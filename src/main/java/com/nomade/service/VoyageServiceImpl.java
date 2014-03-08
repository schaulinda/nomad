package com.nomade.service;

import java.util.List;

import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;


public class VoyageServiceImpl implements VoyageService {
	
 public	 List<Voyage> findByNomade(UserNomade nomad){
		 
		 return voyageRepository.findByNomade(nomad);
	 }
}
