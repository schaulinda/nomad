package com.nomade.domain;


public class BeanNoteBookManager {
	
	private String start;
	private String end;
	
	private double startLng;
	private double startLat;
	
	private double endLng;
	private double endLat;
	
	private EtapeVoyage etapeVoyage = new EtapeVoyage();
	
	private EtapeVehicule etapeVehicule= new EtapeVehicule();
	
	
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

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public double getStartLng() {
		return startLng;
	}

	public void setStartLng(double startLng) {
		this.startLng = startLng;
	}

	public double getStartLat() {
		return startLat;
	}

	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}

	public double getEndLng() {
		return endLng;
	}

	public void setEndLng(double endLng) {
		this.endLng = endLng;
	}

	public double getEndLat() {
		return endLat;
	}

	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}

}
