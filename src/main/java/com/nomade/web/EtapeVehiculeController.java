package com.nomade.web;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nomade.domain.BeanHistorique;
import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.DangerPratique;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;
import com.nomade.service.ParcoursService;
import com.nomade.service.UserService;

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
	@Autowired
	EtapeVoyageService voyageService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	InfoPratiqueService infoPratiqueService;
	@Autowired
	ParcoursService parcoursService;
	
	private void beanHistoriqueDecoration(Model uiModel, UserNomade nomade) {
		List<DangerPratique> listDanger = dangerPratiqueService.findByNomadeOrderByCreated(nomade);
		List<InfoPratique> listInfo = infoPratiqueService.findByNomadeOrderByCreated(nomade);

		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListDanger(listDanger);
		beanHistorique.setListInfo(listInfo);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
	}
	
	@RequestMapping("/save")
	public String save(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		
		EtapeVehicule etapeVeh = beanNoteBookManager.getEtapeVehicule();
		UserNomade nomade = securite.getUserNomade();
		etapeVeh.setNomade(nomade);
		double[] location = new double[]{etapeVeh.getUserlng(), etapeVeh.getUserlat()};
		etapeVeh.setGeolocation(location);
		Parcours lastParcours = parcoursService.lastParcours(nomade);
		if(lastParcours==null){
			beanHistoriqueDecoration(uiModel,nomade);
			beanNoteBookManager.setNotify("nope");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("nomade", nomade);
			uiModel.addAttribute("onglet", "carnet");
			return "public/carnet";
			
		}else{
			etapeVeh.setParcours(lastParcours);
		}
		vehiculeService.saveEtapeVehicule(etapeVeh);
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		beanHistoriqueDecoration(uiModel,nomade);
		bookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}
	
	@RequestMapping("/etapeVehSuiv/{id}/{page}")
	public String nomad(@PathVariable("id")String id, @PathVariable("page")int page ,HttpServletRequest request, Model uiModel) {
		UserNomade nomade = userService.findUserNomade(new BigInteger(id)); 
		//Page<EtapeVehicule> listEtapeVoy = vehiculeService.findByNomade(nomade, page);
		BeanHistorique beanHistorique = new BeanHistorique();
		//beanHistorique.setListEtapeVeh(listEtapeVoy);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		return "public/nomad";
	}
	
}
