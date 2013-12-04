package com.nomade.service;

import java.util.List;

import com.nomade.domain.DangerPratique;


public class DangerPratiqueServiceImpl implements DangerPratiqueService {
	
	
	public List<DangerPratique> findByLocation(){
		
		return dangerPratiqueRepository.findAll();
	}
}
