package com.nomade.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.nomade.domain.Marker;
import com.nomade.domain.Parcours;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.repository.VoyageRepository;


public class ParcoursServiceImpl implements ParcoursService {
	
	
	
	public Parcours lastParcours(UserNomade nomad){
		
		List<Parcours> findByNomadOrderByCreatedDesc = parcoursRepository.findByNomadOrderByCreatedDesc(nomad);
		
		Parcours parcours=null;
		if(findByNomadOrderByCreatedDesc!=null && findByNomadOrderByCreatedDesc.size()>0){
			parcours = findByNomadOrderByCreatedDesc.get(0);
		}
		
		return parcours;
	}
	
	public String buildMakers(List<UserNomade> nomads){
		
		List<Marker> listMarkers = new ArrayList<Marker>();
		Marker mark = null;
		for(UserNomade nomad:nomads){
			Parcours lastParcours = lastParcours(nomad);
			if(lastParcours!=null){
				
				//mark = new Marker(lastParcours.getEndAdress(), nomad.toString());
				mark.setTag("nomad");
				mark.setId(nomad.getId().toString());
				mark.getOptions().setIcon("http://maps.google.com/mapfiles/marker_whiteN.png");
				listMarkers.add(mark);
			}
			
		}
		return Marker.toJsonArray(listMarkers);
	}
	
	public List<Parcours> drawParcours(UserNomade nomad){
		
		return parcoursRepository.findByNomadOrderByCreatedDesc(nomad);
	}
	
	public List<Parcours> findByNomadOrderByCreatedDesc(UserNomade nomad){
		
		return parcoursRepository.findByNomadOrderByCreatedDesc(nomad);
	}
	
	public List<Parcours> findByVoyage(Voyage voyage){
		
		return parcoursRepository.findByVoyage(voyage);
	}
	
	public List<Parcours> findByVoyageAndSortByDayDepart(Voyage voyage){
		
		Sort sort = new Sort(Direction.DESC, "depart.day");
		
		return parcoursRepository.findByVoyage(voyage, sort);
	}
	
	public boolean datesHorsScopeVoyage(Date d1, Date d2, Voyage voyage){
		if(voyage.getStatus().equals(StatusVoyage.TERMINE)){
		if(d1.before(voyage.getDepart().getDay()) || d2.before(voyage.getDepart().getDay())
				|| d1.after(voyage.getArrived().getDay()) || d2.after(voyage.getArrived().getDay())){
			
			return true;
		}
		}
		if(voyage.getStatus().equals(StatusVoyage.EN_COURS)){
			if(d1.before(voyage.getDepart().getDay()) && d2.after(voyage.getDepart().getDay())){
				
				return true;
			}
			}
		return false;
	}
	
	public boolean datesInCollisionWithParcours(Date d1, Date d2, List<Parcours> listP){
		
		for(Parcours p:listP){
			
			if(p.getDepart().getDay().before(d1) && p.getArrived().getDay().after(d1) 
					|| p.getDepart().getDay().before(d2) && p.getArrived().getDay().after(d2)
					|| p.getDepart().getDay().after(d1) && p.getArrived().getDay().before(d2)
					||p.getDepart().getDay().equals(d1) && p.getArrived().equals(d2)){
				
				return true;
			}
			
		}
		
		return false;
	}
}
