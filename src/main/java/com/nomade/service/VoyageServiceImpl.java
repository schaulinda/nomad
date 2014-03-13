package com.nomade.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;

import com.nomade.domain.Etape;
import com.nomade.domain.Parcours;
import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

public class VoyageServiceImpl implements VoyageService {
	
	@Autowired
	ParcoursService parcoursService;

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
	
	public List<Etape> drawParcours(UserNomade nomad) {
		
		ArrayList<Etape> listEtape = new ArrayList<Etape>();
		List<Voyage> listVoyage = voyageRepository.findByNomade(nomad);
		
		for(Voyage v:listVoyage){
			if(v.getDepart().getLocation()!=null && !"".equals(v.getDepart().getLocation())){
				listEtape.add(v.getDepart());
			}
			if(v.getArrived().getLocation()!=null && !"".equals(v.getArrived().getLocation())){
				listEtape.add(v.getArrived());
			}
			System.out.print("v: "+v);
			List<Parcours> byVoyage = parcoursService.findByVoyage(v);
			System.out.print("byVoyage: "+byVoyage);
			for(Parcours p:byVoyage){
				listEtape.add(p.getDepart());
				listEtape.add(p.getArrived());
				listEtape.addAll(p.getEtapes());
			}
			
		}
				
		return sortListEtape(listEtape);
	}

}
