package com.nomade.web;

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
import com.nomade.domain.DangerPratique;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.Relation;
import com.nomade.domain.RoleName;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;
import com.nomade.service.ParcoursService;
import com.nomade.service.RelationService;
import com.nomade.service.UserService;
import com.nomade.service.VoyageService;
import com.nomade.tools.ImageUtilInterface;

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
	@Autowired
	ImageUtilInterface imgService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String selectPage(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();

		if (nomade != null) {
			Set<RoleName> roleNames = nomade.getRoleNames();
			if (roleNames.contains(RoleName.ROLE_ADMIN)) {
				return "redirect:/admin";
			}
			if (roleNames.contains(RoleName.ROLE_SIMPLE_USER)) {
				
				uiModel.addAttribute("fieldPercent", request.getSession(false).getAttribute("fieldPercent"));
				request.getSession(false).removeAttribute("fieldPercent");
				
				BeanNomadeManager beanNomadeManager = new BeanNomadeManager();
				
				beanHistoriqueDecoration(uiModel, nomade, 0);
				
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
			
			beanHistoriqueDecoration(uiModel, nomade, 0);

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
	
private void beanHistoriqueDecoration(Model uiModel, UserNomade nomade, int page) {
		
		BeanHistorique beanHistorique = new BeanHistorique();
		
		beanHistorique.setListInfo(infoPratiqueService.findByNomadeOrderByCreated(nomade));
		beanHistorique.setListDanger(dangerPratiqueService.findByNomadeOrderByCreated(nomade));
		
		beanHistorique.setListImg(imgService.allImg(nomade));
		List<Relation> findMyFriends = relationService.findMyFriends(nomade);
		beanHistorique.setFriends(findMyFriends);
		

		Page<Voyage> voyages = voyageService.findByNomade(nomade, page);
		beanHistorique.setVoyages(voyages);
		
		try {
			Voyage voyage = voyages.getContent().get(0);
			beanHistorique.setListEtapeVoy(etapeVoyageService.findByVoyage(voyage));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			List<EtapeVoyage> findByVoyageNull = etapeVoyageService.findByVoyage(null);
			beanHistorique.getListEtapeVoy().addAll(findByVoyageNull);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
	}

}
