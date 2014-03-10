package com.nomade.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

@RooService(domainTypes = { com.nomade.domain.Parcours.class })
public interface ParcoursService {
	
	public Parcours lastParcours(UserNomade nomad);
	
	public String buildMakers(List<UserNomade> nomads);
	
	public List<Parcours> drawParcours(UserNomade nomad);
	
	public List<Parcours> findByNomadOrderByCreatedDesc(UserNomade nomad);
	
	public List<Parcours> findByVoyage(Voyage voyage);
	
	public List<Parcours> findByVoyageAndSortByDayDepart(Voyage voyage);
	
	public boolean datesHorsScopeVoyage(Date d1, Date d2, Voyage voyage);
	
	public boolean datesInCollisionWithParcours(Date d1, Date d2, List<Parcours> listP);
	
}
