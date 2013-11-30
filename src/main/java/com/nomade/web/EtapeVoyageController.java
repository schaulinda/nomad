package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.UserService;

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

	@RequestMapping("/save")
	public String save(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		EtapeVoyage etapeVoyage = beanNoteBookManager.getEtapeVoyage();
		UserNomade nomade = securite.getUserNomade();
		etapeVoyage.setNomade(nomade);
		double[] location = new double[]{etapeVoyage.getUserlng(), etapeVoyage.getUserlat()};
		etapeVoyage.setGeolocation(location);
		voyageService.saveEtapeVoyage(etapeVoyage);
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}

}
