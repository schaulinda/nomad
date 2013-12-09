package com.nomade;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Parcours;

@RooService(domainTypes = { com.nomade.domain.Parcours.class })
public interface ParcoursService {
	
	public Parcours lastParcours();
}
