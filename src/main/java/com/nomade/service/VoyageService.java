package com.nomade.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

@RooService(domainTypes = { com.nomade.domain.Voyage.class })
public interface VoyageService {
	
	public	List<Voyage> findByNomade(UserNomade nomad);
	
	
}
