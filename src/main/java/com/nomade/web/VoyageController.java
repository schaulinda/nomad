package com.nomade.web;

import java.math.BigInteger;
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
import org.springframework.web.bind.annotation.ResponseBody;

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

		if (findByNomade != null && findByNomade.size() > 0) {

			BeanNoteBookManager bookManager = new BeanNoteBookManager();
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			return "voyages/carnet";
		} else {
			// la page selectView napparait que si le nomad na effectue aucun
			// voyage
			return "voyages/selectView";

		}

	}

	@RequestMapping("/select")
	public String select(HttpServletRequest request, Model uiModel,
			@RequestParam("type") String type) {

		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");

		if ("preparation".equals(type)) {

			BeanNoteBookManager bookManager = new BeanNoteBookManager();
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			return "voyages/carnet";
		}

		if ("nouveau".equals(type)) {

			Voyage voyage = new Voyage();
			uiModel.addAttribute("voyage", voyage);
			return "voyages/new";

		}

		return "voyages/selectView";
	}

	@RequestMapping("/voyager")
	public String voyager(HttpServletRequest request, Model uiModel) {

		UserNomade nomade = securite.getUserNomade();
		Voyage voyage = new Voyage();
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("voyage", voyage);
		return "voyages/new";

	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, Model uiModel,
			@RequestParam("idV") String idV) {

		Voyage voyage = voyageService.findVoyage(new BigInteger(idV));
		voyageService.deleteVoyage(voyage);

		UserNomade nomade = securite.getUserNomade();
		List<Voyage> listVoy = voyageService.findByNomade(nomade);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("listVoy", listVoy);
		return "voyages/listV";
	}

	@RequestMapping("/formTermineVoy")
	public String termineVoyage(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		List<Voyage> list = voyageService.findByNomadeAndStatus(nomade,
				StatusVoyage.EN_COURS);
		uiModel.addAttribute("nomade", nomade);

		if (list != null && list.size() > 0) {
			Voyage voyage = list.get(0);
			voyage.setTerminated(true);
			uiModel.addAttribute("voyage", voyage);
			return "voyages/formTermineVoy";
		} else {
			BeanNoteBookManager bookManager = new BeanNoteBookManager();
			bookManager.setError("Vous n'avez aucun voyage en cour! creer un nouveau voyage");
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			uiModel.addAttribute("voyage",  new Voyage());
			return "voyages/new";
		}

	}
	
	@RequestMapping("/updateAndCloseVoy")
	public String updateAndCloseVoy(HttpServletRequest request, Model uiModel,Voyage voyage) {
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		
		if(voyage.getArrived().getDay()==null || voyage.getArrived().getLocation()==null){
			
			bookManager.setError("La date et la localisation ne doivent pas etre null");
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			uiModel.addAttribute("voyage", voyage);
			return "voyages/formTermineVoy";
		}else{
			
			Voyage voyage2 = voyageService.findByNomadeAndStatus(nomade,StatusVoyage.EN_COURS).get(0);
			voyage2.getArrived().setDay(voyage.getArrived().getDay());
			voyage2.getArrived().setLat(voyage.getArrived().getLat());
			voyage2.getArrived().setLng(voyage.getArrived().getLng());
			voyage2.setTerminated(true);
			voyage2.setStatus(StatusVoyage.TERMINE);
			voyage2.getArrived().setLocation(voyage.getArrived().getLocation());
			voyageService.updateVoyage(voyage2);
			
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			return "voyages/carnet";
		}
		
		
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model uiModel) {

		UserNomade nomade = securite.getUserNomade();
		List<Voyage> listVoy = voyageService.findByNomade(nomade);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("listVoy", listVoy);

		return "voyages/listV";
	}

	@RequestMapping("/save")
	public String select(HttpServletRequest request, Model uiModel,
			Voyage voyage) {
		UserNomade nomade = securite.getUserNomade();
		List<Voyage> voyageEnCours = voyageService.findVoyageEnCours(nomade);

		if (voyageEnCours != null && voyageEnCours.size() > 0) {
			if (voyage.isTerminated()
					&& voyage.getArrived().getLocation() != null
					&& voyage.getArrived().getDay() != null) {

				// voy en cours existant, le voy actuel doit etre termine sans
				// collision
				if (voyageService.collision(voyage.getDepart().getDay(), voyage
						.getArrived().getDay(), nomade) == false) {

					// pas de collision avec les existants voyage
					voyage.setStatus(StatusVoyage.TERMINE);
					voyage.setNomade(nomade);
					voyageService.saveVoyage(voyage);
					BeanNoteBookManager bookManager = new BeanNoteBookManager();
					bookManager.setNotify("yep");
					uiModel.addAttribute("beanNoteBookManager", bookManager);
					return "voyages/carnet";

				} else {// collison avec ancien voy

					BeanNoteBookManager bookManager = new BeanNoteBookManager();
					bookManager
							.setError("les dates entrees correspondent aux dates d'ancien voyage");
					uiModel.addAttribute("beanNoteBookManager", bookManager);
					uiModel.addAttribute("voyage", voyage);
					return "voyages/new";
				}

			} else { // voyage en cours existant et pas de cloture sur le nvo
						// voyage, renvoi sur la page de voy

				BeanNoteBookManager bookManager = new BeanNoteBookManager();
				bookManager
						.setError("Vous devriez terminez votre voyage en cours, en mettant votre statut a Rentre a la maison");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("voyage", voyage);
				return "voyages/new";
			}
		} else {// aucun voyage en cours existant, faut juste verifier les dates
				// avec dautre voy termine pr pas de collison

			if (voyageService.collision(voyage.getDepart().getDay(), voyage
					.getArrived().getDay(), nomade) == false) {
				
				if (voyage.isTerminated()
						&& voyage.getArrived().getLocation() != null
						&& voyage.getArrived().getDay() != null) {
					
					voyage.setStatus(StatusVoyage.TERMINE);
					
				}else{
					voyage.setStatus(StatusVoyage.EN_COURS);
				}

				// pas de collision avec les existants voyage
				
				voyage.setNomade(nomade);
				voyageService.saveVoyage(voyage);
				BeanNoteBookManager bookManager = new BeanNoteBookManager();
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				bookManager.setNotify("yep");
				return "voyages/carnet";

			} else {// collison avec ancien voy

				BeanNoteBookManager bookManager = new BeanNoteBookManager();
				bookManager
						.setError("les dates entrees correspondent aux dates d'ancien voyage");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("voyage", voyage);
				return "voyages/new";
			}

		}
	}

}
