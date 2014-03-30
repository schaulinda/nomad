package com.nomade.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.DangerPratique;
import com.nomade.domain.UserNomade;

@RooService(domainTypes = { com.nomade.domain.DangerPratique.class })
public interface DangerPratiqueService {
	
	public List<DangerPratique> findByLocation(double[] loc1, double[] loc2);
	public Page<DangerPratique> findByNomade(UserNomade nomade, int page);
	public boolean hasVoted(UserNomade nomade, DangerPratique dangerPratique);
	public List<DangerPratique> findByNomadeOrderByCreated(UserNomade nomade);
}
