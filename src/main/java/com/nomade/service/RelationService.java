package com.nomade.service;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.UserNomade;

@RooService(domainTypes = { com.nomade.domain.Relation.class })
public interface RelationService {
	
	public boolean friendschip(UserNomade nomade, UserNomade nomade1);
}
