package com.nomade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Box;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.nomade.domain.DangerPratique;
import com.nomade.domain.InfoPratique;


public class InfoPratiqueServiceImpl implements InfoPratiqueService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	
	public List<InfoPratique> findByLocation(double[] loc1, double[] loc2){
		
		/*Box box = new Box(new Point(loc1[0], loc1[1]), new Point(loc2[0], loc2[1])); 
		
		List<InfoPratique> find = mongoTemplate.find(new Query(Criteria.where("geoLocation").within(box)), InfoPratique.class);
		System.out.print(find);
		return find;*/
		return infoPratiqueRepository.findAll();
		
	}
}
