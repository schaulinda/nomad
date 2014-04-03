package com.nomade.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

@RooService(domainTypes = { com.nomade.domain.EtapeVoyage.class })
public interface EtapeVoyageService {
	
	public Page<EtapeVoyage> findByNomade(UserNomade nomade, int page);
	
	public List<EtapeVoyage> findByVoyage(Voyage voyage);
	
	
}
