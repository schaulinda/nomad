package com.nomade.web;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nomade.domain.BeanNomadeManager;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.RoleName;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.ParcoursService;
import com.nomade.service.RelationService;
import com.nomade.service.UserService;

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
	UserService userService;
	@Autowired
	ParcoursService parcoursService;
	@Autowired
	RelationService relationService;
	
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
				
				Page<EtapeVoyage> findByNomade = etapeVoyageService.findByNomade(nomade, 0);
				Page<EtapeVehicule> findByNomade2 = etapeVehiculeService.findByNomade(nomade, 0);
				beanNomadeManager.setListEtapeVoy(findByNomade);
				beanNomadeManager.setListEtapeVeh(findByNomade2);
				
				List<UserNomade> findAllUserNomades = userService.findAllUserNomades();
				beanNomadeManager.setNomads(findAllUserNomades);	
				beanNomadeManager.setMe(true);
				beanNomadeManager.setHome(true);
				beanNomadeManager.setNomade(nomade);
				String makers = parcoursService.buildMakers(findAllUserNomades);
				beanNomadeManager.setMakers(makers);
				
				uiModel.addAttribute("beanNomadeManager", beanNomadeManager);
				uiModel.addAttribute("nomade", nomade);
				uiModel.addAttribute("onglet", "nomad");
				
				uiModel.addAttribute("demands", relationService.findReceivedDemand(nomade));
				
				return "public/nomad";
			}

		}
		
		return "/login";
	}

}
