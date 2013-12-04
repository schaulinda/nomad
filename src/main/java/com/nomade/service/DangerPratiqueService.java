package com.nomade.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.DangerPratique;

@RooService(domainTypes = { com.nomade.domain.DangerPratique.class })
public interface DangerPratiqueService {
	
	public List<DangerPratique> findByLocation();
}
