package com.nomade.service;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

@RooService(domainTypes = { com.nomade.domain.Voyage.class })
public interface VoyageService {
	
	public	List<Voyage> findByNomade(UserNomade nomad);
	public List<Voyage> findByNomadeAndStatus(UserNomade nomad, StatusVoyage status);
	public List<Voyage> findVoyageEnCours(UserNomade nomad);
	public boolean collision(Date depart, Date arrive, UserNomade nomad);
	public boolean collision(Date depart, UserNomade nomad);
	
}
