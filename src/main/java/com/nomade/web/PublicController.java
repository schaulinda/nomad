package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;

@RequestMapping({ "/public" })
@Controller
public class PublicController {

	@Autowired
	Security securite ;
	
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
		uiModel.addAttribute("onglet", "itineraire");
		return "public/itineraire";
	}
	
	@RequestMapping("/carnet")
	public String carnet(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		
		uiModel.addAttribute("etapeVoyage", new EtapeVoyage());
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}

}
