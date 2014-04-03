package com.nomade.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

public class EtapeVoyageServiceImpl implements EtapeVoyageService {
	
	protected static final int NUMBER_PER_PAGE = 1;
	
	public Page<EtapeVoyage> findByNomade(UserNomade nomade, int page){
		
		/*PageRequest pageRequest = new PageRequest(page, NUMBER_PER_PAGE, new Sort(
			    new Order(Direction.DESC, "depart.day"), 
			    new Order(Direction.DESC, "created")
			  ));*/
		
		PageRequest pageRequest = new PageRequest(page, NUMBER_PER_PAGE, new Sort(Direction.DESC, "depart.day"));

		
		return etapeVoyageRepository.findByNomade(nomade, pageRequest);
	}
	
	public List<EtapeVoyage> findByVoyage(Voyage voyage){
		
		return etapeVoyageRepository.findByVoyage(voyage);
	}
}
