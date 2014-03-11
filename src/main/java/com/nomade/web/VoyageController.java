package com.nomade.web;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.Etape;
import com.nomade.domain.Parcours;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.security.Security;
import com.nomade.service.ParcoursService;
import com.nomade.service.VoyageService;
import com.nomade.tools.IdGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
	@Autowired
	ParcoursService parcoursService;

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
			Parcours parcours = parcoursService.findByVoyageAndSortByDayDepart(voyage2).get(0);//get last parcours
			if(voyage.getArrived().getDay().after(parcours.getArrived().getDay())){
				
				bookManager.setError("La date d'arrive du voyage doit etre plus future que tous ses parcours");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("voyage", voyage);
				return "voyages/formTermineVoy";
				
			}
			
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
	
	@RequestMapping("/listEtape")
	public String listEtape(HttpServletRequest request, Model uiModel, @RequestParam("idP") String idP) {

		UserNomade nomade = securite.getUserNomade();
		Parcours parcours = parcoursService.findParcours(new BigInteger(idP));
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("idP", idP);
		uiModel.addAttribute("listE", parcours.getEtapes());
		uiModel.addAttribute("idV", parcours.getVoyage().getId());
		uiModel.addAttribute("etape", new Etape());
		return "voyages/listE";
	}
	
	@RequestMapping("/deleteEtape/{idP}")
	public String deleteEtape(HttpServletRequest request, Model uiModel,
			@RequestParam("idE") String idE, @PathVariable("idP") String idP) {

		Parcours parcours = parcoursService.findParcours(new BigInteger(idP));
		parcours.setNbreEtape(parcours.getNbreEtape()-1);
		
		for(Etape e:parcours.getEtapes()){
			
			if(e.getCode().equals(idE)){
				 
				parcours.getEtapes().remove(e);
				break;
			}
		}
			
		parcoursService.updateParcours(parcours);
		
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("idP", idP);
		uiModel.addAttribute("listE", parcours.getEtapes());
		uiModel.addAttribute("idV", parcours.getVoyage().getId());
		uiModel.addAttribute("etape", new Etape());
		return "voyages/listE";
	}
	
	@RequestMapping("/createEtape")
	public String createEtape(HttpServletRequest request, Model uiModel,Etape etape, @RequestParam("idP") String idP) {
		
		Parcours parcours = parcoursService.findParcours(new BigInteger(idP));
		uiModel.addAttribute("listE", parcours.getEtapes());
		uiModel.addAttribute("idV", parcours.getVoyage().getId());
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("idP", idP);
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		
		if("".equals(etape.getLocation())){
			
			beanNoteBookManager.setError("entrer une location");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("etape", etape);
			return "voyages/listE";
		}
		
		if(etape.getDay()==null){
			etape.setDay(parcours.getDepart().getDay());
		}
		
		if(etape.getDay().before(parcours.getDepart().getDay()) || etape.getDay().after(parcours.getArrived().getDay()) ){
			
			beanNoteBookManager.setError("la date doit etre dans l'intervalle du parcours");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("etape", etape);
			return "voyages/listE";
		}
		
		etape.setCode(IdGenerator.generateId());
		parcours.getEtapes().add(etape);
		parcours.setNbreEtape(parcours.getNbreEtape()+1);
		parcoursService.updateParcours(parcours);
		Parcours parcours2 = parcoursService.findParcours(new BigInteger(idP));
		uiModel.addAttribute("listE", parcours2.getEtapes());
		beanNoteBookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		uiModel.addAttribute("etape", new Etape());
		return "voyages/listE";
	}
	
	@RequestMapping("/listParcours")
	public String listParcours(HttpServletRequest request, Model uiModel, @RequestParam("idV") String idV) {

		UserNomade nomade = securite.getUserNomade();
		Voyage voyage = voyageService.findVoyage(new BigInteger(idV));
		List<Parcours> listP = parcoursService.findByVoyageAndSortByDayDepart(voyage);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("idV", idV);
		uiModel.addAttribute("listP", listP);
		uiModel.addAttribute("parcours", new Parcours());
		return "voyages/listP";
	}
	
	
	@RequestMapping("/deleteParcours/{idV}")
	public String deleteParcours(HttpServletRequest request, Model uiModel,
			@RequestParam("idP") String idP, @PathVariable("idV") String idV) {

		Parcours parcours = parcoursService.findParcours(new BigInteger(idP));
		parcoursService.deleteParcours(parcours);

		UserNomade nomade = securite.getUserNomade();
		Voyage voyage = voyageService.findVoyage(new BigInteger(idV));
		voyage.setNbreParcours(voyage.getNbreParcours() - 1);
		voyService.saveVoyage(voyage);
		List<Parcours> listP = parcoursService.findByVoyageAndSortByDayDepart(voyage);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("idV", idV);
		uiModel.addAttribute("listP", listP);
		uiModel.addAttribute("parcours", new Parcours());
		return "voyages/listP";
	}
	
	
	@RequestMapping("/createParcours")
	public String createParcours(HttpServletRequest request, Model uiModel,Parcours parcours, @RequestParam("idV") String idV) {
		
		UserNomade nomade = securite.getUserNomade();
		Voyage voyage = voyageService.findVoyage(new BigInteger(idV));
	
		List<Parcours> listP = parcoursService.findByVoyageAndSortByDayDepart(voyage);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("listP", listP);
		uiModel.addAttribute("idV", idV);
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		
		if(parcours.getDepart().getDay()==null || parcours.getArrived().getDay()==null
				|| "".equals(parcours.getDepart().getLocation())|| "".equals(parcours.getArrived().getLocation())){
			
			beanNoteBookManager.setError("Completer tous les champs!");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("parcours", parcours);
			uiModel.addAttribute("listP", listP);
			return "voyages/listP";
		}
			
		
		if(parcoursService.datesHorsScopeVoyage(parcours.getDepart().getDay(), parcours.getArrived().getDay(), voyage)){
			beanNoteBookManager.setError("Les dates du parcours ne sont pas compris dans le voyage");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("parcours", parcours);
			uiModel.addAttribute("listP", listP);
			return "voyages/listP";
		}
		
		if(parcoursService.datesInCollisionWithParcours(parcours.getDepart().getDay(), parcours.getArrived().getDay(), listP)){
			beanNoteBookManager.setError("Les dates du parcours correspondent ou s'intefere a un autre parcours");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("parcours", parcours);
			uiModel.addAttribute("listP", listP);
			return "voyages/listP";
		}
		
		voyage.setNbreParcours(voyage.getNbreParcours() + 1);
		voyageService.updateVoyage(voyage);	
		parcours.setVoyage(voyage);
		parcoursService.saveParcours(parcours);
		Voyage voyage2 = voyageService.findVoyage(new BigInteger(idV));
		beanNoteBookManager.setNotify("yep");
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		uiModel.addAttribute("parcours", new Parcours());
		System.out.print("voyage2: "+voyage2);
		List<Parcours> listP1 = parcoursService.findByVoyageAndSortByDayDepart(voyage2);
		uiModel.addAttribute("listP", listP1);
		return "voyages/listP";
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
