package com.nomade.web;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nomade.domain.BeanHistorique;
import com.nomade.domain.DangerPratique;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;
import com.nomade.service.ParcoursService;

@RequestMapping("/infopratiques")
@Controller
@RooWebScaffold(path = "infopratiques", formBackingObject = InfoPratique.class)
public class InfoPratiqueController {
	
	@Autowired
	Security securite ;
	@Autowired
	EtapeVoyageService voyageService;
	@Autowired
	EtapeVehiculeService vehiculeService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	ParcoursService parcoursService;
	
	
	private void beanHistoriqueDecoration(Model uiModel, UserNomade nomade) {
		Page<EtapeVoyage> listEtapeVoy = voyageService.findByNomade(nomade, 0);
		Page<EtapeVehicule> listEtapeVeh = vehiculeService.findByNomade(nomade, 0);
		Page<DangerPratique> listDanger = dangerPratiqueService.findByNomade(nomade, 0);
		Page<InfoPratique> listInfo = infoPratiqueService.findByNomade(nomade, 0);
		
		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListEtapeVoy(listEtapeVoy);
		beanHistorique.setListEtapeVeh(listEtapeVeh);
		beanHistorique.setListDanger(listDanger);
		beanHistorique.setListInfo(listInfo);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
	}


    @RequestMapping("/save")
    public String save(InfoPratique infoPratique,  Model uiModel, HttpServletRequest request) {
    	
    	UserNomade userNomade = securite.getUserNomade(); 
    		
    	double[] location = new double[]{infoPratique.getLocationLng(), infoPratique.getLocationLat()};
    	infoPratique.setGeoLocation(location);
		infoPratique.setNomade(userNomade);
    	infoPratiqueService.saveInfoPratique(infoPratique);
    	beanHistoriqueDecoration(uiModel,userNomade);
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
    
    @RequestMapping("/infoSuiv/{id}/{page}")
   	public String nomad(@PathVariable("id")String id, @PathVariable("page")int page ,HttpServletRequest request, Model uiModel) {
   		UserNomade nomade = userService.findUserNomade(new BigInteger(id)); 
   		Page<InfoPratique> listDanger = infoPratiqueService.findByNomade(
   				nomade, page);
   		BeanHistorique beanHistorique = new BeanHistorique();
   		beanHistorique.setListInfo(listDanger);
   		beanHistorique.setNomade(nomade);
   		uiModel.addAttribute("beanHistorique", beanHistorique);
   		return "public/nomad";
   	}
}
