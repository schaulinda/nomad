package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
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
	Security securite ;
	@Autowired
	UserService userService;
	
	@RequestMapping("/save")
	public String save(BeanNoteBookManager beanNoteBookManager, HttpServletRequest request, Model uiModel) {
		EtapeVoyage etapeVoyage = beanNoteBookManager.getEtapeVoyage();
		System.out.print(etapeVoyage.toString());
		UserNomade nomade = securite.getUserNomade();
		nomade.getEtapeVoyages().add(etapeVoyage);
		userService.updateUserNomade(nomade);
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager",bookManager);
		uiModel.addAttribute("nomade", securite.getUserNomade());
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}
	
	
}
