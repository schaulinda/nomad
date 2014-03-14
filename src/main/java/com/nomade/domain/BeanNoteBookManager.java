package com.nomade.domain;

import java.util.List;


public class BeanNoteBookManager {
	
	
	private List<Etape> etapes;
	
	private EtapeVoyage etapeVoyage = new EtapeVoyage();
	
	private EtapeVehicule etapeVehicule= new EtapeVehicule();
	
	private boolean voyageEnCours;
	
	
	private String notify;
	
	private String error;
	
	private List<Etape> listParcours;

	public EtapeVoyage getEtapeVoyage() {
		return etapeVoyage;
	}

	public void setEtapeVoyage(EtapeVoyage etapeVoyage) {
		this.etapeVoyage = etapeVoyage;
	}

	public EtapeVehicule getEtapeVehicule() {
		return etapeVehicule;
	}

	public void setEtapeVehicule(EtapeVehicule etapeVehicule) {
		this.etapeVehicule = etapeVehicule;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public List<Etape> getListParcours() {
		return listParcours;
	}

	public void setListParcours(List<Etape> listParcours) {
		this.listParcours = listParcours;
	}

	public List<Etape> getEtapes() {
		return etapes;
	}

	public void setEtapes(List<Etape> etapes) {
		this.etapes = etapes;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean isVoyageEnCours() {
		return voyageEnCours;
	}

	public void setVoyageEnCours(boolean voyageEnCours) {
		this.voyageEnCours = voyageEnCours;
	}

}
