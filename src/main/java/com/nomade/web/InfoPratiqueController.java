package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.InfoPratique;
import com.nomade.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/infopratiques")
@Controller
@RooWebScaffold(path = "infopratiques", formBackingObject = InfoPratique.class)
public class InfoPratiqueController {
	
	@Autowired
	Security securite ;


    @RequestMapping("/save")
    public String save(InfoPratique infoPratique,  Model uiModel, HttpServletRequest request) {
    	
    	double[] location = new double[]{infoPratique.getLocationLng(), infoPratique.getLocationLat()};
    	infoPratique.setGeoLocation(location);
    	infoPratique.setNomade(securite.getUserNomade());
    	infoPratiqueService.saveInfoPratique(infoPratique);
    	uiModel.addAttribute("infoPratique", new InfoPratique());
    	uiModel.addAttribute("saveInfoDanger", "saveInfoDanger");
    	return "public/info";
    }
}
