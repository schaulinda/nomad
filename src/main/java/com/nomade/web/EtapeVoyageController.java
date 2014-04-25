package com.nomade.web;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.nomade.domain.Etape;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.Parcours;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.EtapeService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;
import com.nomade.service.ParcoursService;
import com.nomade.service.UserService;
import com.nomade.service.VoyageService;

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
	@Autowired
	EtapeService etapeService;
	@Autowired
	VoyageService voyageService2;
	
	
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
		
				
		EtapeVoyage etapeVoyage = beanNoteBookManager.getEtapeVoyage();
		
		//date
		if(etapeVoyage.getDateEtape()==null){
			etapeVoyage.setDateEtape(new Date());
		}
		//nomad
		UserNomade nomade = securite.getUserNomade();
		etapeVoyage.setNomade(nomade);
		
		//location
		double[] location = new double[]{etapeVoyage.getUserlat(), etapeVoyage.getUserlng()};
		etapeVoyage.setGeolocation(location);
		
		boolean existingVoyage = voyageService2.existingVoyage(nomade);
		if(existingVoyage){
			etapeVoyage.setVoyage(voyageService2.findVoyageEnCours(nomade).get(0));
		}else{
			etapeVoyage.setVoyage(null);
		}
		
		
		//save and render page
		voyageService.saveEtapeVoyage(etapeVoyage);
		
		beanHistoriqueDecoration(uiModel, nomade);
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setNotify("yep");
		bookManager.setVoyageEnCours(existingVoyage);
		//bookManager.setListParcours(etapeService.drawParcours(nomade));
		uiModel.addAttribute("beanNoteBookManager", bookManager);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		return "voyages/carnet";
	}
	
	
	@RequestMapping("/saveEtape")
	public String saveEtape(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		
		 Map<String, String[]> parameters = request.getParameterMap();
		 UserNomade nomade = securite.getUserNomade();
		 
		 List<Etape> listEtape = new ArrayList<Etape>();
		 
		    for(String key : parameters.keySet()) {
		        
		        if(!key.equals("fragments") && !key.equals("ajaxSource")){
		        	
		        	String[] vals = parameters.get(key);
		        
		        if(!vals[0].equals("")){
		        	
		        	Etape etape = new Etape();
		        	//etape.setNomad(nomade);
		        	etape.setLocation(vals[0]);
		        	try {
						double double1 = Double.parseDouble(vals[1]);
						double double2 = Double.parseDouble(vals[2]);
						etape.setLat(double1);
						etape.setLng(double2);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	if(vals[3]!=null && vals[3]!=""){
		        		etape.setDateEtape(vals[3]);
		        	}
		        	
		        	
		        	etapeService.saveEtape(etape);
		        }
		        
		        for(String val : vals){
		            System.out.println(" -> " + val);
		         }
		        }
		    	
		    }
		    
		    BeanNoteBookManager bookManager = new BeanNoteBookManager();
		    beanHistoriqueDecoration(uiModel, nomade);
			
			bookManager.setNotify("yep");
			//bookManager.setListParcours(etapeService.drawParcours(nomade));
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			uiModel.addAttribute("nomade", nomade);
			uiModel.addAttribute("onglet", "carnet");
			return "public/carnet";
	}
	
	@RequestMapping("/etapeVoySuiv/{id}/{page}")
	public String nomad(@PathVariable("id")String id, @PathVariable("page")int page ,HttpServletRequest request, Model uiModel) {
		UserNomade nomade = userService.findUserNomade(new BigInteger(id)); 
		//List<EtapeVoyage> listEtapeVoy = etapeVoyageService.
		BeanHistorique beanHistorique = new BeanHistorique();
		//beanHistorique.setListEtapeVoy(listEtapeVoy);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		return "public/nomad";
	}
	
	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("etapeVoyage_dateetape_date_format", "dd-MM-yyyy");
        uiModel.addAttribute("etapeVoyage_created_date_format", "dd-MM-yyyy HH:mm");
    }
	
	void populateEditForm(Model uiModel, EtapeVoyage etapeVoyage) {
        uiModel.addAttribute("etapeVoyage", etapeVoyage);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("usernomades", userService.findAllUserNomades());
    }

}
