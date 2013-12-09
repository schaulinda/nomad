package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import com.nomade.ParcoursService;
import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.UserService;
import com.nomade.service.VehiculeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/etapevoyages")
@Controller
@RooWebScaffold(path = "etapevoyages", formBackingObject = EtapeVoyage.class)
public class EtapeVoyageController {

	@Autowired
	Security securite;
	@Autowired
	UserService userService;
	@Autowired
	EtapeVoyageService voyageService;
	@Autowired
	EtapeVehiculeService vehiculeService;
	@Autowired
	ParcoursService parcoursService;

	@RequestMapping("/save")
	public String save(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		EtapeVoyage etapeVoyage = beanNoteBookManager.getEtapeVoyage();
		UserNomade nomade = securite.getUserNomade();
		etapeVoyage.setNomade(nomade);
		double[] location = new double[]{etapeVoyage.getUserlng(), etapeVoyage.getUserlat()};
		etapeVoyage.setGeolocation(location);
		
		Parcours lastParcours = parcoursService.lastParcours();
		if(lastParcours==null){
			beanNoteBookManager.setListEtapeVoy(voyageService.findAllEtapeVoyages());
			beanNoteBookManager.setListEtapeVeh(vehiculeService.findAllEtapeVehicules());
			beanNoteBookManager.setNotify("nope");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("nomade", nomade);
			uiModel.addAttribute("onglet", "carnet");
			return "public/carnet";
			
		}else{
			etapeVoyage.setParcours(lastParcours);
		}
		voyageService.saveEtapeVoyage(etapeVoyage);
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setListEtapeVoy(voyageService.findAllEtapeVoyages());
		bookManager.setListEtapeVeh(vehiculeService.findAllEtapeVehicules());
		bookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}
	
	@RequestMapping("/saveParcours")
	public String saveParcours(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		
		double[] location1 = new double[]{beanNoteBookManager.getStartLng(), beanNoteBookManager.getStartLat()};
		double[] location2 = new double[]{beanNoteBookManager.getEndLng(), beanNoteBookManager.getEndLat()};
		
		Parcours parcours = new Parcours(location1, location2);
		parcoursService.saveParcours(parcours);
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setListEtapeVoy(voyageService.findAllEtapeVoyages());
		bookManager.setListEtapeVeh(vehiculeService.findAllEtapeVehicules());
		bookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", securite.getUserNomade());
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
		
	}

}
