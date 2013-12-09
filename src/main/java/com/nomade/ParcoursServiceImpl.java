package com.nomade;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.nomade.domain.Parcours;


public class ParcoursServiceImpl implements ParcoursService {
	
	public Parcours lastParcours(){
		
		Iterable<Parcours> all = parcoursRepository.findAll(new Sort(Direction.DESC, "created"));
		
		Parcours next;
		try {
			next = all.iterator().next();
		} catch (Exception e) {
			next=null;
		}
		
		
		return next;
	}
}
