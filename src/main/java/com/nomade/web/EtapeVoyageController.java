package com.nomade.web;

import java.math.BigInteger;
import java.util.Date;

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
	@Autowired
	EtapeVehiculeService vehiculeService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	InfoPratiqueService infoPratiqueService;
	@Autowired
	ParcoursService parcoursService;
	
	
	private void beanHistoriqueDecoration(Model uiModel, UserNomade nomade) {
		Page<EtapeVoyage> listEtapeVoy = voyageService.findByNomade(
				nomade, 0);
		Page<EtapeVehicule> listEtapeVeh = vehiculeService.findByNomade(
				nomade, 0);
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
	public String save(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		
		System.out.print("beanNoteBookManager: "+beanNoteBookManager);
		saveParcours(beanNoteBookManager);
		
		EtapeVoyage etapeVoyage = beanNoteBookManager.getEtapeVoyage();
		
		if(etapeVoyage.getDateEtape()==null){
			etapeVoyage.setDateEtape(new Date());
		}
		
		UserNomade nomade = securite.getUserNomade();
		etapeVoyage.setNomade(nomade);
		
		double[] location = new double[]{etapeVoyage.getUserlng(), etapeVoyage.getUserlat()};
		etapeVoyage.setGeolocation(location);
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		
		Parcours lastParcours = parcoursService.lastParcours(nomade);
		if(lastParcours==null){
			beanNoteBookManager.setNotify("nope");
			bookManager.setListParcours(parcoursService.drawParcours(nomade));
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			uiModel.addAttribute("nomade", nomade);
			uiModel.addAttribute("onglet", "carnet");
			
			beanHistoriqueDecoration(uiModel, nomade);
			
			return "public/carnet";
			
		}else{
			etapeVoyage.setParcours(lastParcours);
		}
		voyageService.saveEtapeVoyage(etapeVoyage);
		
		beanHistoriqueDecoration(uiModel, nomade);
		
		bookManager.setNotify("yep");
		bookManager.setListParcours(parcoursService.drawParcours(nomade));
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "public/carnet";
	}
	
	
	private void saveParcours(BeanNoteBookManager beanNoteBookManager) {
		
		if(beanNoteBookManager.getStart()!=null && beanNoteBookManager.getEnd()!=null){
				
			double[] location1 = new double[]{beanNoteBookManager.getStartLng(), beanNoteBookManager.getStartLat()};
			double[] location2 = new double[]{beanNoteBookManager.getEndLng(), beanNoteBookManager.getEndLat()};
			
			UserNomade userNomade = securite.getUserNomade();
			
			Parcours parcours = new Parcours(location1, location2);
			parcours.setStartAdress(beanNoteBookManager.getStart());
			parcours.setEndAdress(beanNoteBookManager.getEnd());
			parcours.setNomad(userNomade);
			parcoursService.saveParcours(parcours);
		}
		
	}
	
	@RequestMapping("/etapeVoySuiv/{id}/{page}")
	public String nomad(@PathVariable("id")String id, @PathVariable("page")int page ,HttpServletRequest request, Model uiModel) {
		UserNomade nomade = userService.findUserNomade(new BigInteger(id)); 
		Page<EtapeVoyage> listEtapeVoy = etapeVoyageService.findByNomade(
				nomade, page);
		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListEtapeVoy(listEtapeVoy);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		return "public/nomad";
	}

}
