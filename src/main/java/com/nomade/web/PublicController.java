package com.nomade.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nomade.domain.BeanManagerItineraire;
import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.DangerPratique;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.InfoPratiqueService;

@RequestMapping({ "/public" })
@Controller
public class PublicController {

	@Autowired
	Security securite ;
	@Autowired
	InfoPratiqueService infoPratiqueService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	
	@RequestMapping("/nomad")
	public String nomad(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "nomad");
		return "public/nomad";
	}
	
	@RequestMapping("/itineraire")
	public String itineraire(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("beanManagerItineraire", new BeanManagerItineraire());
		uiModel.addAttribute("onglet", "itineraire");
		return "public/itineraire";
	}
	
	@RequestMapping("/findInfosDangers")
	public String findInfosDangers(BeanManagerItineraire beanManagerItineraire, HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		uiModel.addAttribute("nomade", nomade);
		double[] loc1 = new double[]{beanManagerItineraire.getStartLng(), beanManagerItineraire.getStartLat()};
		double[] loc2 = new double[]{beanManagerItineraire.getEndLng(), beanManagerItineraire.getEndLat()};
		List<InfoPratique> listInfo = infoPratiqueService.findByLocation(loc1,loc2);
		beanManagerItineraire.setInfoPratiques(listInfo);
		List<DangerPratique> listDanger = dangerPratiqueService.findByLocation(loc1,loc2);
		beanManagerItineraire.setDangerPratiques(listDanger);
		beanManagerItineraire.setBol("true");
		beanManagerItineraire.buildMakers();
		uiModel.addAttribute("beanManagerItineraire", beanManagerItineraire);
		uiModel.addAttribute("onglet", "itineraire");
		return "public/itineraire";
	}
	
	@RequestMapping("/carnet")
	public String carnet(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		uiModel.addAttribute("beanNoteBookManager", new BeanNoteBookManager());
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}
	
	@RequestMapping("/danger")
	public String danger(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("dangerPratique", new DangerPratique());
		return "public/danger";
	}
	
	@RequestMapping("/info")
	public String info(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
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

}
