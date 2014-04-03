package com.nomade.web;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.Etape;
import com.nomade.domain.Parcours;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.VehiculeState;
import com.nomade.domain.Voyage;
import com.nomade.security.Security;
import com.nomade.service.ParcoursService;
import com.nomade.service.UserService;
import com.nomade.service.VoyageService;
import com.nomade.tools.IdGenerator;

import org.cloudfoundry.org.codehaus.jackson.annotate.JsonAnySetter;
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
	UserService service;
	
	

	@RequestMapping("/selectView")
	public String selectView(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		List<Voyage> findByNomade = voyService.findByNomade(nomade);

		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());

		if (findByNomade != null && findByNomade.size() > 0) {

			beanNoteBookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
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
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());

		if ("preparation".equals(type)) {

			beanNoteBookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
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
		uiModel.addAttribute("onglet", "carnet");
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		return "voyages/new";

	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, Model uiModel,
			@RequestParam("idV") String idV) {

		Voyage voyage = voyageService.findVoyage(new BigInteger(idV));
		voyageService.deleteVoyage(voyage);
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);

		UserNomade nomade = securite.getUserNomade();
		List<Voyage> listVoy = voyageService.findByNomade(nomade);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("listVoy", listVoy);
		return "voyages/listV";
	}

	@RequestMapping("/formTermineVoy")
	public String termineVoyage(HttpServletRequest request, Model uiModel) {
		UserNomade nomade = securite.getUserNomade();
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		List<Voyage> list = voyageService.findByNomadeAndStatus(nomade,
				StatusVoyage.EN_COURS);
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("onglet", "carnet");
		if (list != null && list.size() > 0) {
			Voyage voyage = list.get(0);
			voyage.setTerminated(true);
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("voyage", voyage);
			return "voyages/formTermineVoy";
		} else {
			
			beanNoteBookManager.setError("Vous n'avez aucun voyage en cour! creer un nouveau voyage");
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("voyage",  new Voyage());
			return "voyages/new";
		}

	}
	
	@RequestMapping("/updateAndCloseVoy")
	public String updateAndCloseVoy(HttpServletRequest request, Model uiModel,Voyage voyage) {
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setListParcours(voyageService.drawVoyageEnCours());
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		
		if(voyage.getArrived().getDay()==null || voyage.getArrived().getLocation()==null){
			
			bookManager.setError("La date et la localisation ne doivent pas etre null");
			uiModel.addAttribute("beanNoteBookManager", bookManager);
			uiModel.addAttribute("voyage", voyage);
			return "voyages/formTermineVoy";
		}else{
			Voyage voyage2 = voyageService.findByNomadeAndStatus(nomade,StatusVoyage.EN_COURS).get(0);
			List<Parcours> listParcours = parcoursService.findByVoyageAndSortByDayDepart(voyage2);
			
			if(listParcours !=null && listParcours.size() > 0){//si existe un parcours
				
			
				Parcours parcours = listParcours.get(0);//get last parcours
				if(voyage.getArrived().getDay().before(parcours.getArrived().getDay())){
					
					bookManager.setError("La date d'arrive du voyage doit etre plus future que tous ses parcours");
					uiModel.addAttribute("beanNoteBookManager", bookManager);
					uiModel.addAttribute("voyage", voyage);
					return "voyages/formTermineVoy";
					
				}
			}
			
			voyage2.getArrived().setDay(voyage.getArrived().getDay());
			voyage2.getArrived().setLat(voyage.getArrived().getLat());
			voyage2.getArrived().setLng(voyage.getArrived().getLng());
			voyage2.setTerminated(true);
			voyage2.setStatus(StatusVoyage.TERMINE);
			voyage2.getArrived().setLocation(voyage.getArrived().getLocation());
			voyageService.updateVoyage(voyage2);
			
			bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
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
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);

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
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		
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
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		
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
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		
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
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		
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
		
		BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		
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
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		
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
		beanNoteBookManager.setListParcours(voyageService.drawVoyageEnCours());
		uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
		uiModel.addAttribute("parcours", new Parcours());
		List<Parcours> listP1 = parcoursService.findByVoyageAndSortByDayDepart(voyage2);
		uiModel.addAttribute("listP", listP1);
		return "voyages/listP";
	}

	@RequestMapping("/save")
	public String select(HttpServletRequest request, Model uiModel,
			Voyage voyage) {
		UserNomade nomade = securite.getUserNomade();
		List<Voyage> voyageEnCours = voyageService.findVoyageEnCours(nomade);
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setListParcours(voyageService.drawVoyageEnCours());
		
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
					bookManager.setNotify("yep");
					bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
					uiModel.addAttribute("beanNoteBookManager", bookManager);
					return "voyages/carnet";

				} else {// collison avec ancien voy

					
					bookManager
							.setError("les dates entrees correspondent aux dates d'ancien voyage");
					uiModel.addAttribute("beanNoteBookManager", bookManager);
					uiModel.addAttribute("voyage", voyage);
					return "voyages/new";
				}

			} else { // voyage en cours existant et pas de cloture sur le nvo
						// voyage, renvoi sur la page de voy
				bookManager
						.setError("Vous devriez terminez votre voyage en cours, en mettant votre statut a Rentre a la maison");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("voyage", voyage);
				return "voyages/new";
			}
		} else {// aucun voyage en cours existant, faut juste verifier les dates
				// avec dautre voy termine pr pas de collison
			if (voyage.isTerminated()
					&& voyage.getArrived().getLocation() != null
					&& voyage.getArrived().getDay() != null) {
			
			if (voyageService.collision(voyage.getDepart().getDay(), voyage
					.getArrived().getDay(), nomade) == false) {
				
				
				// pas de collision avec les existants voyage
				voyage.setStatus(StatusVoyage.TERMINE);
				voyage.setNomade(nomade);
				voyageService.saveVoyage(voyage);
				bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
				bookManager.setNotify("yep");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				return "voyages/carnet";

			} else {// collison avec ancien voy

				bookManager.setError("les dates entrees correspondent aux dates d'ancien voyage");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("voyage", voyage);
				return "voyages/new";
			}
		}else{//aucun voyage existant et le voyage actuel ne pas termine
			
			if (voyageService.collision(voyage.getDepart().getDay(), nomade) == false) {
				
				
				// pas de collision avec les existants voyage
				voyage.setStatus(StatusVoyage.EN_COURS);
				nomade.getVehicule().setVehiculeState(VehiculeState.onTheRoad);
				userService.updateUserNomade(nomade);
				voyage.setNomade(nomade);
				voyageService.saveVoyage(voyage);
				bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
				bookManager.setNotify("yep");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				return "voyages/carnet";

			} else {// collison avec ancien voy

				bookManager.setError("la date de depart appartient a dans l'intervalle de temps d'un autre voyage");
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("voyage", voyage);
				return "voyages/new";
			}
		}

		}
	}
	
	@RequestMapping("/saveEtape")
	public String saveEtape(BeanNoteBookManager beanNoteBookManager,
			HttpServletRequest request, Model uiModel) {
		
		BeanNoteBookManager bookManager = new BeanNoteBookManager();
		bookManager.setListParcours(voyageService.drawVoyageEnCours());
		
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
						
						bookManager.setError("Selectionnez une localisation sur la map a l'aide du mappicker");
						bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
						uiModel.addAttribute("beanNoteBookManager", bookManager);
						uiModel.addAttribute("nomade", nomade);
						uiModel.addAttribute("onglet", "carnet");
						return "voyages/carnet";
						
					}
		        	if(vals[3]==null && "".equals(vals[3])){
						bookManager.setError("Entrer des dates pr vos etapes");
						bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
						uiModel.addAttribute("beanNoteBookManager", bookManager);
						uiModel.addAttribute("nomade", nomade);
						uiModel.addAttribute("onglet", "carnet");
						return "voyages/carnet";
		        	}
		        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		        	Date d;
					try {
						d = sdf.parse(vals[3]);
						etape.setDay(d);
					} catch (ParseException e) {
						bookManager.setError("vous n'avez pas entrer la date dans le bon format");
						bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
						uiModel.addAttribute("beanNoteBookManager", bookManager);
						uiModel.addAttribute("nomade", nomade);
						uiModel.addAttribute("onglet", "carnet");
						return "voyages/carnet";
					}
		        	
		        	etape.setCode(IdGenerator.generateId());
		        	listEtape.add(etape);
		        	
		        }
		        
		        for(String val : vals){
		            System.out.println(" -> " + val);
		         }
		        }
		    	
		    }
		    //after boucle
		    Voyage voyage = voyageService.findByNomadeAndStatus(nomade, StatusVoyage.EN_COURS).get(0);
		    List<Etape> sortListEtape = voyageService.sortListEtape(listEtape);
		    if(voyage.getDepart().getDay().before(sortListEtape.get(0).getDay()) 
		    		|| voyage.getDepart().getDay().equals((sortListEtape.get(0).getDay()))){
		    	
			    Parcours parcours = new Parcours();
			    parcours.setDepart(sortListEtape.get(0));
			    parcours.setArrived(sortListEtape.get(sortListEtape.size()-1));
			    int length = sortListEtape.size()-2;
			    for(int i=1; i<= length; i++){
			    	parcours.getEtapes().add(sortListEtape.get(i));
			    }
			    
			    
			    voyage.setNbreParcours(voyage.getNbreParcours()+1);
			    voyageService.updateVoyage(voyage);
			    
			    parcours.setVoyage(voyage);
			    parcours.setNbreEtape(parcours.getEtapes().size());
			    parcours.setNomad(nomade);
			    parcoursService.saveParcours(parcours);
			    
			    bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
				bookManager.setNotify("yep");
				bookManager.setListParcours(voyageService.drawVoyageEnCours());
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("nomade", nomade);
				uiModel.addAttribute("onglet", "carnet");
				return "voyages/carnet";
		    }else{
		    	
				bookManager.setError("la date des etapes doivent etre egal ou plus future que la date de depart du voyage");
				bookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
				bookManager.setListParcours(voyageService.drawVoyageEnCours());
				uiModel.addAttribute("beanNoteBookManager", bookManager);
				uiModel.addAttribute("nomade", nomade);
				uiModel.addAttribute("onglet", "carnet");
				return "voyages/carnet";
		    	
		    }
	}
	
	@RequestMapping("/visualiser/{idV}")
	public @ResponseBody String  visu(HttpServletRequest request, @PathVariable("idV") String idV ) {
		
		List<Etape> oneParcours = voyageService.drawOneParcours(idV);
		
		return Etape.toJsonArray(oneParcours);
		
	}

}
