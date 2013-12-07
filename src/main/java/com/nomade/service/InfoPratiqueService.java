package com.nomade.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.DangerPratique;
import com.nomade.domain.InfoPratique;

@RooService(domainTypes = { com.nomade.domain.InfoPratique.class })
public interface InfoPratiqueService {
	
	public List<InfoPratique> findByLocation(double[] loc1, double[] loc2);
	
}
