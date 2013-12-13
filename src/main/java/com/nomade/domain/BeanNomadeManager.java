package com.nomade.domain;

import java.util.List;

import org.springframework.data.domain.Page;


public class BeanNomadeManager {
	
	private UserNomade nomade;
	
	private Page<EtapeVoyage> listEtapeVoy;
	
	private Page<EtapeVehicule> listEtapeVeh;
	
	List<UserNomade> nomads;
	
	private boolean amie;
	
	private boolean me;
	
	private String makers;

	
	public UserNomade getNomade() {
		return nomade;
	}

	public void setNomade(UserNomade nomade) {
		this.nomade = nomade;
	}

	public Page<EtapeVoyage> getListEtapeVoy() {
		return listEtapeVoy;
	}

	public void setListEtapeVoy(Page<EtapeVoyage> listEtapeVoy) {
		this.listEtapeVoy = listEtapeVoy;
	}

	public Page<EtapeVehicule> getListEtapeVeh() {
		return listEtapeVeh;
	}

	public void setListEtapeVeh(Page<EtapeVehicule> listEtapeVeh) {
		this.listEtapeVeh = listEtapeVeh;
	}

	public boolean isAmie() {
		return amie;
	}

	public void setAmie(boolean amie) {
		this.amie = amie;
	}

	public String getMakers() {
		return makers;
	}

	public void setMakers(String makers) {
		this.makers = makers;
	}

	public boolean isMe() {
		return me;
	}

	public void setMe(boolean me) {
		this.me = me;
	}

	public List<UserNomade> getNomads() {
		return nomads;
	}

	public void setNomads(List<UserNomade> nomads) {
		this.nomads = nomads;
	}
	
}
