package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@RequestMapping("/save")
	public String save(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		
		EtapeVehicule etapeVeh = beanNoteBookManager.getEtapeVehicule();
		UserNomade nomade = securite.getUserNomade();
		etapeVeh.setNomade(nomade);
		double[] location = new double[]{etapeVeh.getUserlng(), etapeVeh.getUserlat()};
		etapeVeh.setGeolocation(location);
		vehiculeService.saveEtapeVehicule(etapeVeh);
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}
	
}
