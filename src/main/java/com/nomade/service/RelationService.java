package com.nomade.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;

@RooService(domainTypes = { com.nomade.domain.Relation.class })
public interface RelationService {
	
	public boolean friendschip(UserNomade nomade, UserNomade nomade1);
	
	public List<Relation> findByNomadeOrNomade2(UserNomade nomade,UserNomade nomade1);
}
