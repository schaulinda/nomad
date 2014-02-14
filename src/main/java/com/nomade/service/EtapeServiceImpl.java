package com.nomade.service;

import java.util.ArrayList;
import java.util.List;


import com.nomade.domain.Etape;
import com.nomade.domain.Marker;
import com.nomade.domain.UserNomade;

public class EtapeServiceImpl implements EtapeService {
	
	
	public Etape lastEtape(UserNomade nomad) {
		List<Etape> findByNomadOrderByDay = etapeRepository.findByNomadOrderByDayDesc(nomad);
		
		Etape etape=null;
		if(findByNomadOrderByDay!=null && findByNomadOrderByDay.size()>0){
			etape = findByNomadOrderByDay.get(0);
		}
		
		return etape;
	}
	

	public String buildMakers(List<UserNomade> nomads) {
		List<Marker> listMarkers = new ArrayList<Marker>();
		Marker mark = null;
		for(UserNomade nomad:nomads){
			Etape lastEtape = lastEtape(nomad);
			if(lastEtape!=null){
				
				mark = new Marker(lastEtape.getLocation(), nomad.toString());
				mark.setTag("nomad");
				mark.setId(nomad.getId().toString());
				mark.getOptions().setIcon("http://maps.google.com/mapfiles/marker_whiteN.png");
				listMarkers.add(mark);
			}
			
		}
		return Marker.toJsonArray(listMarkers);
	}
	
	
	public List<Etape> drawParcours(UserNomade nomad) {
		
		return etapeRepository.findByNomadOrderByDayDesc(nomad);
	}
}
