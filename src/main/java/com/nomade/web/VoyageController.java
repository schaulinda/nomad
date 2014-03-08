package com.nomade.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.security.Security;
import com.nomade.service.VoyageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/voyages")
@Controller
@RooWebScaffold(path = "voyages", formBackingObject = Voyage.class)
public class VoyageController {
	
	@Autowired
	Security securite;
	@Autowired
	VoyageService voyService;
	
	@RequestMapping("/selectView")
	public String selectView(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		List<Voyage> findByNomade = voyService.findByNomade(nomade);
		
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		
		if(findByNomade!=null && findByNomade.size()>0){
			
			BeanNoteBookManager bookManager = new BeanNoteBookManager();
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			return "voyages/carnet";
		}else{
			
			return "voyages/selectView";
			
		}
		
	}
	
	@RequestMapping("/select")
	public String select(HttpServletRequest request, Model uiModel, 
			@RequestParam("type") String type) {
		
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		
		if("preparation".equals(type)){
			
			Voyage voyage = new Voyage();
			voyage.setStatus(StatusVoyage.EN_PREPARATION);
			voyageService.saveVoyage(voyage);
			
			BeanNoteBookManager bookManager = new BeanNoteBookManager();
			bookManager.getEtapeVoyage().setVoyage(voyage);
			uiModel.addAttribute("beanNoteBookManager", bookManager);	
			return "voyages/carnet";
		}
		
		if("nouveau".equals(type)){
			
			Voyage voyage = new Voyage();
			uiModel.addAttribute("voyage", voyage);
			return "voyages/new";
			
		}	
		
		return "voyages/selectView";
	}
	
	@RequestMapping("/save")
	public String select(HttpServletRequest request, Model uiModel, Voyage voyage) {
		
		voyageService.saveVoyage(voyage);
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.getEtapeVoyage().setVoyage(voyage);
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		return "voyages/carnet";
	}
	
}

