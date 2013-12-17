package com.nomade;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;

@RooService(domainTypes = { com.nomade.domain.Parcours.class })
public interface ParcoursService {
	
	public Parcours lastParcours(UserNomade nomad);
	
	public String buildMakers(List<UserNomade> nomads);
	
}
