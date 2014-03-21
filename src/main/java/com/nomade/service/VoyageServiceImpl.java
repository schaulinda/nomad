package com.nomade.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;

import com.nomade.domain.Etape;
import com.nomade.domain.Marker;
import com.nomade.domain.Parcours;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import com.nomade.security.Security;

public class VoyageServiceImpl implements VoyageService {

	@Autowired
	ParcoursService parcoursService;
	@Autowired
	Security security;
	@Autowired
	UserService userService;
	
private String linkBase(HttpServletRequest httpServletRequest){
		
		StringBuilder stringBuilder = new StringBuilder()
		.append("http://")
		.append(httpServletRequest.getServerName())
		.append(":").append(httpServletRequest.getServerPort())
		.append("/resources/img")
		.append("/mapicon/veh");
		
		return stringBuilder.toString();
		
	}

	public List<Voyage> findByNomade(UserNomade nomad) {

		return voyageRepository.findByNomade(nomad);
	}

	public List<Voyage> findByNomadeAndStatus(UserNomade nomad,
			StatusVoyage status) {

		return voyageRepository.findByNomadeAndStatus(nomad, status);
	}

	public List<Voyage> findVoyageEnCours(UserNomade nomad) {

		return voyageRepository.findByNomadeAndStatus(nomad,
				StatusVoyage.EN_COURS);
	}

	public boolean collision(Date depart, Date arrive, UserNomade nomad) {

		List<Voyage> list = voyageRepository.findByNomadeAndStatus(nomad,
				StatusVoyage.TERMINE);
		boolean bool = false;
		for (Voyage v : list) {

			if (v.getDepart().getDay().before(depart)
					&& v.getArrived().getDay().after(depart)
					|| v.getDepart().getDay().before(arrive)
					&& v.getArrived().getDay().after(arrive)
					|| v.getDepart().getDay().after(depart)
					&& v.getArrived().getDay().before(arrive)
					|| v.getDepart().getDay().equals(depart)
					&& v.getArrived().equals(arrive)) {

				bool = true;
				break;

			} else {

				bool = false;
			}

		}

		return bool;
	}

	public boolean collision(Date depart, UserNomade nomad) {

		List<Voyage> list = voyageRepository.findByNomadeAndStatus(nomad,
				StatusVoyage.TERMINE);
		boolean bool = false;
		for (Voyage v : list) {

			if (v.getDepart().getDay().before(depart)
					&& v.getArrived().getDay().after(depart)) {

				bool = true;
				break;

			} else {

				bool = false;
			}

		}

		return bool;
	}

	public List<Etape> sortListEtape(List<Etape> listToSort) {

		try {

			Collections.sort(listToSort, new BeanComparator("day"));

			return listToSort;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public boolean existingVoyage(UserNomade nomade) {

		List<Voyage> listVoyage = findByNomadeAndStatus(nomade,
				StatusVoyage.EN_COURS);
		if (listVoyage != null && listVoyage.size() > 0) {
			return true;
		}

		return false;
	}

	public List<Etape> drawAllParcours(UserNomade nomad) {

		ArrayList<Etape> listEtape = new ArrayList<Etape>();
		List<Voyage> listVoyage = voyageRepository.findByNomade(nomad);

		for (Voyage v : listVoyage) {
			if (v.getDepart().getLocation() != null
					&& !"".equals(v.getDepart().getLocation())) {
				listEtape.add(v.getDepart());
			}
			if (v.getArrived().getLocation() != null
					&& !"".equals(v.getArrived().getLocation())) {
				listEtape.add(v.getArrived());
			}
		
			List<Parcours> byVoyage = parcoursService.findByVoyage(v);
			
			for (Parcours p : byVoyage) {
				listEtape.add(p.getDepart());
				listEtape.add(p.getArrived());
				listEtape.addAll(p.getEtapes());
			}

		}

		return sortListEtape(listEtape);
	}

	public List<Etape> drawOneParcours(String idV) {

		ArrayList<Etape> listEtape = new ArrayList<Etape>();
		Voyage v = voyageRepository.findOne(new BigInteger(idV));

		if (v.getDepart().getLocation() != null
				&& !"".equals(v.getDepart().getLocation())) {
			listEtape.add(v.getDepart());
		}
		if (v.getArrived().getLocation() != null
				&& !"".equals(v.getArrived().getLocation())) {
			listEtape.add(v.getArrived());
		}

		List<Parcours> listP = parcoursService.findByVoyage(v);

		for (Parcours p : listP) {
			listEtape.add(p.getDepart());
			listEtape.add(p.getArrived());
			listEtape.addAll(p.getEtapes());
		}

		return sortListEtape(listEtape);
	}
	
	
	public List<Etape> drawVoyageEnCours(){
		UserNomade nomade = security.getUserNomade();
		List<Voyage> findByNomadeAndStatus = voyageRepository.findByNomadeAndStatus(nomade, StatusVoyage.EN_COURS);
		if(findByNomadeAndStatus!=null && findByNomadeAndStatus.size()>0){
			Voyage voyage = findByNomadeAndStatus.get(0);
			List<Etape> drawOneParcours = drawOneParcours(""+voyage.getId());
			System.out.print("drawVoyageEnCours: "+drawOneParcours.toString());
			
			return drawOneParcours;
		}else
			return null;
		
	}
	

	public Etape getLastLocation(UserNomade nomad) {

		List<Etape> allEtape = drawAllParcours(nomad);

		if(allEtape!=null && allEtape.size() > 0)
			return allEtape.get(0);
		else
			return null;
	}
	
	public List<Marker>  buildNomadMakers(HttpServletRequest httpServletRequest) {
		
		List<UserNomade> findAllUserNomades = userService.findAllUserNomades();
		String linkBase = linkBase(httpServletRequest);
		List<Marker> listMarkers = new ArrayList<Marker>();
		Marker mark = null;
		for(UserNomade nomad:findAllUserNomades){
			Etape lastEtape = getLastLocation(nomad);
			if(lastEtape!=null){
				double[] latLng = {lastEtape.getLat(), lastEtape.getLng()};
				 mark = new Marker(latLng, nomad.toString());
		
				mark.setTag("nomad");
				mark.setId(nomad.getId().toString());
				String icon = linkBase+"/"+nomad.getVehicule().getIcon()+".png";
				//mark.getOptions().setIcon(icon);
				mark.getOptions().setIcon("http://maps.google.com/mapfiles/marker_whiteN.png");
				listMarkers.add(mark);
			}
			
		}
		return listMarkers;
	}

}
