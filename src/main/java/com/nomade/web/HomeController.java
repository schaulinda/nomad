package com.nomade.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nomade.domain.BeanHistorique;
import com.nomade.domain.BeanNomadeManager;
import com.nomade.domain.BeanTopicManager;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.DangerPratique;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.RoleName;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.security.SecurityUtil;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.DiscussionService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;
import com.nomade.service.ParcoursService;
import com.nomade.service.RelationService;
import com.nomade.service.SubTopicService;
import com.nomade.service.TopicService;
import com.nomade.service.UserService;
import com.nomade.service.VoyageService;

@RequestMapping({ "/", "/index", "home" })
@Controller
public class HomeController {

	@Autowired
	Security securite ;
	@Autowired
	EtapeVoyageService etapeVoyageService;
	@Autowired
	EtapeVehiculeService etapeVehiculeService;
	@Autowired
	InfoPratiqueService infoPratiqueService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	UserService userService;
	@Autowired
	ParcoursService parcoursService;
	@Autowired
	RelationService relationService;
	@Autowired
	VoyageService voyageService;
	private static final String PAGE_SIZE = "5";

	private static final String FORUM_SUBTOPICS = "/forum/subtopics";

	private static final String FORUM_TOPICS = "/forum/topics";

	@Autowired
	private TopicService topicService;

	@Autowired
	private SubTopicService subTopicService;

	@Autowired
	private DiscussionService discussionService;

	@Autowired
	SecurityUtil securityUtil;
	
	@RequestMapping(method = RequestMethod.GET)
	public String selectPage(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();

		if (nomade != null) {
			Set<RoleName> roleNames = nomade.getRoleNames();
			if (roleNames.contains(RoleName.ROLE_ADMIN)) {
				return "redirect:/admin";
			}
			if(roleNames.contains(RoleName.ROLE_MODERATOR)){
				return "redirect:/admin";
			}
			if (roleNames.contains(RoleName.ROLE_SIMPLE_USER)) {
				
				uiModel.addAttribute("fieldPercent", request.getSession(false).getAttribute("fieldPercent"));
				request.getSession(false).removeAttribute("fieldPercent");
				
				BeanNomadeManager beanNomadeManager = new BeanNomadeManager();
				
				beanHistoriqueDecoration(uiModel, nomade);
				
				beanNomadeManager.setMarker(voyageService.buildNomadMakers(request));	
				beanNomadeManager.setMe(true);
				//beanNomadeManager.setHome(true);
				beanNomadeManager.setNomade(nomade);
				//String makers = parcoursService.buildMakers(findAllUserNomades);
				//beanNomadeManager.setMakers(makers);
				uiModel.addAttribute("beanNomadeManager", beanNomadeManager);
				uiModel.addAttribute("nomade", nomade);
				uiModel.addAttribute("onglet", "nomad");
				
				uiModel.addAttribute("demands", relationService.findReceivedDemand(nomade));
				
				return "public/nomad";
			}

		}
		
		return "/login";
	}
	
	@RequestMapping("/admin")
	public String adminController(HttpServletRequest request, Model uiModel){
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("fieldPercent", request.getSession(false).getAttribute("fieldPercent"));
		request.getSession(false).removeAttribute("fieldPercent");
		
		BeanNomadeManager beanNomadeManager = new BeanNomadeManager();
		
		Page<EtapeVoyage> listEtapeVoy = etapeVoyageService.findByNomade(nomade, 0);
		Page<EtapeVehicule> listEtapeVeh = etapeVehiculeService.findByNomade(nomade, 0);
		Page<DangerPratique> listDanger = dangerPratiqueService.findByNomade(nomade, 0);
		Page<InfoPratique> listInfo = infoPratiqueService.findByNomade(nomade, 0);
		
		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListEtapeVoy(listEtapeVoy.getContent());//listEtapeVoy.getContent().get(0).getComments().s
		beanHistorique.setListEtapeVeh(listEtapeVeh.getContent());
		beanHistorique.setListDanger(listDanger.getContent());
		beanHistorique.setListInfo(listInfo.getContent());
		beanHistorique.setNomade(nomade);
		
		List<UserNomade> findAllUserNomades = userService.findAllUserNomades();
		beanNomadeManager.setNomads(findAllUserNomades);	
		beanNomadeManager.setMe(true);
		//beanNomadeManager.setHome(true);
		beanNomadeManager.setNomade(nomade);
		//String makers = parcoursService.buildMakers(findAllUserNomades);
		//beanNomadeManager.setMakers(makers);
		
		
		List<Topic> topics = new ArrayList<Topic>();
		topics = findTopicsDependingIfUserIsLoggedOrNot();
		List<BeanTopicManager> topicBeans = new ArrayList<BeanTopicManager>();
		for (Topic topic : topics) {
			List<SubTopic> subTopics = new ArrayList<SubTopic>();
			if(securityUtil.isUserLogged() && securityUtil.hasAccessToFrozenData()){
				subTopics =  subTopicService.findByParentTopic(topic);
			}else if(securityUtil.isUserLogged() && !securityUtil.hasAccessToFrozenData()){
				subTopics =  subTopicService.findByParentTopicAndFrozen(topic, Boolean.FALSE);
			}else{
				subTopics = subTopicService.findByParentTopicAndConfidentialityAndFrozen(topic, Confidentiality.Publique, Boolean.FALSE);
			}
			topic.setSubTopics(new HashSet<SubTopic>(subTopics));
			BeanTopicManager beanTopicManager = new BeanTopicManager();
			beanTopicManager.setTopic(topic);
			beanTopicManager.setNbOfDiscussion(topicService
					.countDiscussion(topic));
			beanTopicManager.setNbOfMessages(topicService.countMessages(topic));
			beanTopicManager.setLastMessageDate(topicService
					.getLastMessageDate(topic));
			topicBeans.add(beanTopicManager);
		}
		uiModel.addAttribute("topicBeans", topicBeans);
//		return "public/forum/mainpage";
		
		uiModel.addAttribute("beanHistorique", beanHistorique);
		uiModel.addAttribute("beanNomadeManager", beanNomadeManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "nomad");
		
		uiModel.addAttribute("demands", relationService.findReceivedDemand(nomade));
		
		return "public/nomad";
	}

	private List<Topic> findTopicsDependingIfUserIsLoggedOrNot() {
		List<Topic> topics;
		if (!securityUtil.isUserLogged()) {
			topics = topicService
					.findByConfidentiality(Confidentiality.Publique);
		} else {
			topics = topicService.findAllTopics();
		}
		return topics;
	}

	@RequestMapping("/@{username}")
	public String nomad(@PathVariable("username") String username, HttpServletRequest request, Model uiModel) {
		
		if(username=="login"){
			return "/login";
		}
		
		UserNomade nomade = securite.getUserNomade();
		
		BeanNomadeManager beanNomadeManager = new BeanNomadeManager();
		List<UserNomade> findByUserName = userService.findByUserName(username);
		
		if(findByUserName!=null && findByUserName.size()>0){
			
			UserNomade findUserNomade = findByUserName.get(0);
			
			beanHistoriqueDecoration(uiModel, findUserNomade);

			beanNomadeManager.setMarker(voyageService.buildNomadMakers(request));
			beanNomadeManager.setMe(false);
			beanNomadeManager.setHome(false);


		try {
			if (nomade.getUserName().equals(findUserNomade.getUserName())) {

				beanNomadeManager.setMe(true);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		beanNomadeManager.setAmie(relationService.friendschip(nomade,
				findUserNomade));
		beanNomadeManager.setNomade(findUserNomade);
		beanNomadeManager.setMarker(voyageService.buildNomadMakers(request));

		uiModel.addAttribute("beanNomadeManager", beanNomadeManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "nomad");
		
		}
		return "public/nomad";
		
	}
	
	private void beanHistoriqueDecoration(Model uiModel, UserNomade nomade) {
		
		List<DangerPratique> listDanger = dangerPratiqueService.findByNomadeOrderByCreated(nomade);
		List<InfoPratique> listInfo = infoPratiqueService.findByNomadeOrderByCreated(nomade);
		
		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListDanger(listDanger);
		beanHistorique.setListInfo(listInfo);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
	}
}
