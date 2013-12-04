package com.nomade.web;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.InfoPratique;
import com.nomade.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @RequestMapping(value = "votePlus/{idInfo}")
	@ResponseBody
	public String votePositif(@PathVariable("idInfo") String idInfo, Model uiModel) {
    	BigInteger bigInteger = new BigInteger(idInfo);
 
    	InfoPratique infoPratique = infoPratiqueService.findInfoPratique(bigInteger);
    	infoPratique.incrementVotePositif();
    	infoPratiqueService.updateInfoPratique(infoPratique);
    	return ""+infoPratique.getVotePositif();
    }
    
    @RequestMapping(value = "voteMinus/{idInfo}")
   	@ResponseBody
   	public String voteMinus(@PathVariable("idInfo") String idInfo, Model uiModel) {
       	BigInteger bigInteger = new BigInteger(idInfo);
    
       	InfoPratique infoPratique = infoPratiqueService.findInfoPratique(bigInteger);
       	infoPratique.incrementVoteNegatif();
       	infoPratiqueService.updateInfoPratique(infoPratique);
       	return ""+infoPratique.getVoteNegatif();
       }
    
    @RequestMapping(value = "detail/{idInfo}")
   	public String detail(@PathVariable("idInfo") String idInfo, Model uiModel) {
       	BigInteger bigInteger = new BigInteger(idInfo);
    
       	InfoPratique infoPratique = infoPratiqueService.findInfoPratique(bigInteger);
       	uiModel.addAttribute("infoPratique", infoPratique);
       	return "public/infoDetail";
       }
}
