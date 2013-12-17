package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.Parcours;
import com.nomade.ParcoursService;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.UserService;

@RequestMapping("/etapevehicules")
@Controller
@RooWebScaffold(path = "etapevehicules", formBackingObject = EtapeVehicule.class)
public class EtapeVehiculeController {
	
	@Autowired
	Security securite ;
	@Autowired
	UserService userService;
	@Autowired
	EtapeVehiculeService vehiculeService;
	@Autowired
	EtapeVoyageService voyageService;
	@Autowired
	ParcoursService parcoursService;
	
	@RequestMapping("/save")
	public String save(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		
		EtapeVehicule etapeVeh = beanNoteBookManager.getEtapeVehicule();
		UserNomade nomade = securite.getUserNomade();
		etapeVeh.setNomade(nomade);
		double[] location = new double[]{etapeVeh.getUserlng(), etapeVeh.getUserlat()};
		etapeVeh.setGeolocation(location);
		Parcours lastParcours = parcoursService.lastParcours(nomade);
		if(lastParcours==null){
			beanNoteBookManager.setListEtapeVoy(voyageService.findAllEtapeVoyages());
			beanNoteBookManager.setListEtapeVeh(vehiculeService.findAllEtapeVehicules());
			beanNoteBookManager.setNotify("nope");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("nomade", nomade);
			uiModel.addAttribute("onglet", "carnet");
			return "public/carnet";
			
		}else{
			etapeVeh.setParcours(lastParcours);
		}
		vehiculeService.saveEtapeVehicule(etapeVeh);
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setListEtapeVoy(voyageService.findAllEtapeVoyages());
		bookManager.setListEtapeVeh(vehiculeService.findAllEtapeVehicules());
		bookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}
	
}
