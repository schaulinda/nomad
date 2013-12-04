package com.nomade.web;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.DangerPratique;
import com.nomade.domain.InfoPratique;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/dangerpratiques")
@Controller
@RooWebScaffold(path = "dangerpratiques", formBackingObject = DangerPratique.class)
public class DangerPratiqueController {
	
	@Autowired
	Security securite ;


    @RequestMapping("/save")
    public String save(DangerPratique dangerPratique,  Model uiModel, HttpServletRequest request) {
    	
    	double[] location = new double[]{dangerPratique.getLocationLng(), dangerPratique.getLocationLat()};
    	dangerPratique.setGeoLocation(location);
    	dangerPratique.setNomade(securite.getUserNomade());
    	dangerPratiqueService.saveDangerPratique(dangerPratique);
    	uiModel.addAttribute("dangerPratique", new DangerPratique());
    	uiModel.addAttribute("saveInfoDanger", "saveInfoDanger");
    	return "public/danger";
    }
    
    @RequestMapping(value = "votePlus/{idInfo}")
	@ResponseBody
	public String votePositif(@PathVariable("idInfo") String idInfo, Model uiModel) {
    	BigInteger bigInteger = new BigInteger(idInfo);
 
    	DangerPratique dangerPratique = dangerPratiqueService.findDangerPratique(bigInteger);
    	dangerPratique.incrementVotePositif();
    	dangerPratiqueService.updateDangerPratique(dangerPratique);
    	return ""+dangerPratique.getVotePositif();
    }
    
    @RequestMapping(value = "voteMinus/{idInfo}")
   	@ResponseBody
   	public String voteMinus(@PathVariable("idInfo") String idInfo, Model uiModel) {
       	BigInteger bigInteger = new BigInteger(idInfo);
       	DangerPratique dangerPratique = dangerPratiqueService.findDangerPratique(bigInteger);
    	dangerPratique.incrementVoteNegatif();;
    	dangerPratiqueService.updateDangerPratique(dangerPratique);
    	return ""+dangerPratique.getVoteNegatif();
       }
    
    @RequestMapping(value = "detail/{idInfo}")
   	public String detail(@PathVariable("idInfo") String idInfo, Model uiModel) {
       	BigInteger bigInteger = new BigInteger(idInfo);
    
       	DangerPratique dangerPratique = dangerPratiqueService.findDangerPratique(bigInteger);
       	uiModel.addAttribute("dangerPratique", dangerPratique);
       	return "public/dangerDetail";
       }
}
