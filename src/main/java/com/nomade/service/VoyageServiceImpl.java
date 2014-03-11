package com.nomade.service;

import java.util.Date;
import java.util.List;

import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.repository.VoyageRepository;


public class VoyageServiceImpl implements VoyageService {
	
	public	 List<Voyage> findByNomade(UserNomade nomad){
		 
		 return voyageRepository.findByNomade(nomad);
	 }

public List<Voyage> findByNomadeAndStatus(UserNomade nomad, StatusVoyage status){
	 
	 return voyageRepository.findByNomadeAndStatus(nomad, status);
}

public List<Voyage> findVoyageEnCours(UserNomade nomad){
	 
	 return voyageRepository.findByNomadeAndStatus(nomad, StatusVoyage.EN_COURS);
}

public boolean collision(Date depart, Date arrive, UserNomade nomad){
	
	List<Voyage> list = voyageRepository.findByNomadeAndStatus(nomad, StatusVoyage.TERMINE);
	boolean bool=false;
	for(Voyage v : list){
		
		if(v.getDepart().getDay().before(depart) && v.getArrived().getDay().after(depart) 
				|| v.getDepart().getDay().before(arrive) && v.getArrived().getDay().after(arrive)
				|| v.getDepart().getDay().after(depart) && v.getArrived().getDay().before(arrive)
				||v.getDepart().getDay().equals(depart) && v.getArrived().equals(arrive)){
			
			bool = true;
			break;
			
		}else{
			
			bool = false;
		}
	
	}
	
	return bool;
}

public boolean collision(Date depart, UserNomade nomad){
	
	List<Voyage> list = voyageRepository.findByNomadeAndStatus(nomad, StatusVoyage.TERMINE);
	boolean bool=false;
	for(Voyage v : list){
		
		if(v.getDepart().getDay().before(depart) && v.getArrived().getDay().after(depart)){
			
			bool = true;
			break;
			
		}else{
			
			bool = false;
		}
	
	}
	
	return bool;
}

}
