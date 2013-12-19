package com.nomade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;


public class RelationServiceImpl implements RelationService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public boolean friendschip(UserNomade nomade, UserNomade nomade1){
		
		List<Relation> findByNomade2AndNomade = relationRepository.findByNomade2AndNomade(nomade, nomade1);
		if(findByNomade2AndNomade!=null && findByNomade2AndNomade.size()>0){
			return true;
		}
		
		List<Relation> findByNomadeAndNomade2 = relationRepository.findByNomadeAndNomade2(nomade, nomade1);
		if(findByNomadeAndNomade2!=null && findByNomadeAndNomade2.size()>0){
			return true;
		}
		
		return false;
	}
	
	public List<Relation> findByNomadeOrNomade2(UserNomade nomade,UserNomade nomade1){
		
		return relationRepository.findByNomadeOrNomade2(nomade,nomade1);
	}
}
