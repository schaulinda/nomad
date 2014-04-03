package com.nomade.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;

@RooService(domainTypes = { com.nomade.domain.InfoPratique.class })
public interface InfoPratiqueService {
	
	public List<InfoPratique> findByLocation(double[] loc1, double[] loc2);
	public Page<InfoPratique> findByNomade(UserNomade nomade, int page);
	public boolean hasVoted(UserNomade nomade,InfoPratique infoPratique);
	public List<InfoPratique> findByNomadeOrderByCreated(UserNomade nomade);
	
}
