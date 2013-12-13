package com.nomade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.nomade.domain.Marker;
import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;


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
				
				mark = new Marker(lastParcours.getEndAdress(), nomad.toString());
				mark.setTag("nomad");
				mark.setId(nomad.getId().toString());
				mark.getOptions().setIcon("http://maps.google.com/mapfiles/marker_whiteN.png");
				listMarkers.add(mark);
			}
			
		}
		return Marker.toJsonArray(listMarkers);
	}
}
