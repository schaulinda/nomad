package com.nomade.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.UserNomade;


public class EtapeVehiculeServiceImpl implements EtapeVehiculeService {
	
protected static final int NUMBER_PER_PAGE = 1;
	
	public Page<EtapeVehicule> findByNomade(UserNomade nomade, int page){
		
		PageRequest pageRequest = new PageRequest(page, NUMBER_PER_PAGE, new Sort(
			    new Order(Direction.DESC, "comments.created"), 
			    new Order(Direction.DESC, "created")
			  ));

		
		return etapeVehiculeRepository.findByNomade(nomade, pageRequest);
	}
	
}
