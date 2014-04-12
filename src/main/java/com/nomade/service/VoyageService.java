package com.nomade.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Marker;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.domain.Etape;

@RooService(domainTypes = { com.nomade.domain.Voyage.class })
public interface VoyageService {
	
	public	List<Voyage> findByNomade(UserNomade nomad);
	public List<Voyage> findByNomadeAndStatus(UserNomade nomad, StatusVoyage status);
	public List<Voyage> findVoyageEnCours(UserNomade nomad);
	public boolean collision(Date depart, Date arrive, UserNomade nomad);
	public boolean collision(Date depart, UserNomade nomad);
	public List<Etape> sortListEtape(List<Etape> listToSort);
	public boolean existingVoyage(UserNomade nomade);
	public List<Etape> drawAllParcours(UserNomade nomad);
	public List<Etape> drawOneParcours(String idV);
	public Etape getLastLocation(UserNomade nomad);
	public List<Etape> drawVoyageEnCours();
	public List<Marker>  buildNomadMakers(HttpServletRequest httpServletRequest);
	public Page<Voyage> findByNomade(UserNomade nomade, int page);
	
}
