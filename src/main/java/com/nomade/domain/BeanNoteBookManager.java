package com.nomade.domain;

import java.util.List;

public class BeanNoteBookManager {
	
	private EtapeVoyage etapeVoyage = new EtapeVoyage();
	
	private EtapeVehicule etapeVehicule= new EtapeVehicule();
	
	private List<EtapeVoyage> listEtapeVoy;
	
	private List<EtapeVehicule> listEtapeVeh;
	
	private String notify;

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

	public List<EtapeVoyage> getListEtapeVoy() {
		return listEtapeVoy;
	}

	public void setListEtapeVoy(List<EtapeVoyage> listEtapeVoy) {
		this.listEtapeVoy = listEtapeVoy;
	}

	public List<EtapeVehicule> getListEtapeVeh() {
		return listEtapeVeh;
	}

	public void setListEtapeVeh(List<EtapeVehicule> listEtapeVeh) {
		this.listEtapeVeh = listEtapeVeh;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

}
