package com.nomade.service;

import org.springframework.data.domain.Page;
import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.UserNomade;

@RooService(domainTypes = { com.nomade.domain.EtapeVehicule.class })
public interface EtapeVehiculeService {
	
	public Page<EtapeVehicule> findByNomade(UserNomade nomade, int page);
}
