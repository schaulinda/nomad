package com.nomade.web;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nomade.domain.BeanHistorique;
import com.nomade.domain.BeanManagerItineraire;
import com.nomade.domain.BeanNomadeManager;
import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.DangerPratique;
import com.nomade.domain.Etape;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.Relation;
import com.nomade.domain.TypeTime;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.EtapeService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;
import com.nomade.service.ParcoursService;
import com.nomade.service.RelationService;
import com.nomade.service.UserService;
import com.nomade.service.VoyageService;
import com.nomade.tools.ImageUtil;
import com.nomade.tools.ImageUtilInterface;

@RequestMapping({ "/public" })
@Controller
public class PublicController {

	@Autowired
	Security securite;
	@Autowired
	InfoPratiqueService infoPratiqueService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	EtapeVoyageService etapeVoyageService;
	@Autowired
	EtapeVehiculeService etapeVehiculeService;
	@Autowired
	UserService userService;
	@Autowired
	ParcoursService parcoursService;
	@Autowired
	RelationService relationService;
	@Autowired
	EtapeService etapeService;
	@Autowired
	VoyageService voyageService;
	@Autowired
	ImageUtilInterface imgService;

	@RequestMapping("/nomad")
	public String nomad(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		BeanNomadeManager beanNomadeManager = new BeanNomadeManager();

		beanHistoriqueDecoration(uiModel, nomade, 0);

		beanNomadeManager.setMe(true);
		beanNomadeManager.setNomade(nomade);
		
		beanNomadeManager.setMarker(voyageService.buildNomadMakers(request));

		
		uiModel.addAttribute("beanNomadeManager", beanNomadeManager);
		
		uiModel.addAttribute("nomade", nomade); 
		uiModel.addAttribute("onglet", "nomad");
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
			List<EtapeVoyage> findByVoyageNull = etapeVoyageService.findByVoyageAndNomade(null,nomade);
			if(beanHistorique.getListEtapeVoy()!=null && beanHistorique.getListEtapeVoy().size()>0)
				beanHistorique.getListEtapeVoy().addAll(findByVoyageNull);
		}
		
		
		
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
	}


	@RequestMapping("/nomad/{id}")
	public String selectNomad(@PathVariable("id") String id,
			HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		BeanNomadeManager beanNomadeManager = new BeanNomadeManager();
		UserNomade findUserNomade = userService.findUserNomade(new BigInteger(id)); 
		
		beanHistoriqueDecoration(uiModel, findUserNomade, 0);

		
		beanNomadeManager.setMe(false);
		beanNomadeManager.setHome(false);


		try {
			if (nomade.getUserName().equals(findUserNomade.getUserName())) {

				beanNomadeManager.setMe(true);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		beanNomadeManager.setAmie(relationService.friendschip(nomade,
				findUserNomade));
		beanNomadeManager.setNomade(findUserNomade);
		Etape etape = voyageService.getLastLocation(nomade);
		//renvoyer le derniere parcours sur la vue
		beanNomadeManager.setMarker(voyageService.buildNomadMakers(request));

		uiModel.addAttribute("beanNomadeManager", beanNomadeManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "nomad");
		return "public/nomad";
	}

	@RequestMapping("/itineraire")
	public String itineraire(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();

		uiModel.addAttribute("nomade", nomade);
		BeanManagerItineraire beanManagerItineraire = new BeanManagerItineraire();
		beanManagerItineraire.setInfoPratiquesAll(infoPratiqueService
				.findAllInfoPratiques());
		beanManagerItineraire.setDangerPratiquesAll(dangerPratiqueService
				.findAllDangerPratiques());
		beanManagerItineraire.buildMakers(request);
		uiModel.addAttribute("beanManagerItineraire", beanManagerItineraire);
		uiModel.addAttribute("onglet", "itineraire");
		return "public/itineraire";
	}

	@RequestMapping("/findInfosDangers")
	public String findInfosDangers(BeanManagerItineraire beanManagerItineraire,
			HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();

		uiModel.addAttribute("nomade", nomade);
		double[] loc1 = new double[] { beanManagerItineraire.getStartLng(),
				beanManagerItineraire.getStartLat() };
		double[] loc2 = new double[] { beanManagerItineraire.getEndLng(),
				beanManagerItineraire.getEndLat() };
		List<InfoPratique> listInfo = infoPratiqueService.findByLocation(loc1,
				loc2);
		beanManagerItineraire.setInfoPratiques(listInfo);
		List<DangerPratique> listDanger = dangerPratiqueService.findByLocation(
				loc1, loc2);
		beanManagerItineraire.setDangerPratiques(listDanger);
		beanManagerItineraire.setBol("true");
		beanManagerItineraire.setInfoPratiquesAll(infoPratiqueService
				.findAllInfoPratiques());
		beanManagerItineraire.setDangerPratiquesAll(dangerPratiqueService
				.findAllDangerPratiques());
		beanManagerItineraire.buildMakers(request);
		
		request.getSession(true).setAttribute("beanManagerItineraire", beanManagerItineraire);
		
		uiModel.addAttribute("beanManagerItineraire", beanManagerItineraire);
		uiModel.addAttribute("onglet", "itineraire");
		return "public/itineraire";
	}
	
	@RequestMapping("/backToItenary")
	public String backToItenary(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);

		BeanManagerItineraire beanManagerItineraire = (BeanManagerItineraire)request.getSession().getAttribute("beanManagerItineraire");
		beanManagerItineraire.setBol("back");
		
		uiModel.addAttribute("beanManagerItineraire", beanManagerItineraire);
		uiModel.addAttribute("onglet", "itineraire");
		return "public/itineraire";
	}
	

	@RequestMapping("/carnet")
	public String carnet(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		beanHistoriqueDecoration(uiModel, nomade, 0);
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}

	@RequestMapping("/danger")
	public String danger(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();

		uiModel.addAttribute("typeTime", Arrays.asList(TypeTime.class.getEnumConstants()));
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("dangerPratique", new DangerPratique());
		return "public/danger";
	}

	@RequestMapping("/info")
	public String info(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();

		
		uiModel.addAttribute("typeTime", Arrays.asList(TypeTime.class.getEnumConstants()));
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("infoPratique", new InfoPratique());

		return "public/info";
	}

	@RequestMapping("/forum")
	public String forum(HttpServletRequest request, Model uiModel) {

		UserNomade nomade = securite.getUserNomade();

		uiModel.addAttribute("nomade", nomade);

		return "public/forum";
	}
	
	@RequestMapping("/{pageName}")
	public String page(@PathVariable("pageName") String pageName, HttpServletRequest request, Model uiModel) {

		if(pageName!=null && !"".equals("pageName"))
			return "footerpages/"+pageName;
		else
			return "/";
	}

}
