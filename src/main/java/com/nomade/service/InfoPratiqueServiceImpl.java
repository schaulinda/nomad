package com.nomade.service;

import java.util.List;

import com.nomade.domain.InfoPratique;


public class InfoPratiqueServiceImpl implements InfoPratiqueService {
	
	
	
	public List<InfoPratique> findInfoByLocation(){
		
		return infoPratiqueRepository.findAll();
	}
}
