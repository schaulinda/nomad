package com.nomade.domain;

import java.util.List;

public class BeanManagerItineraire {
	
	private double startLng;
	private double startLat;
	
	private double stepOverLng;
	private double stepOverLat;
	
	private double endLng;
	private double endLat;
	
	private String itineraire;
	
	private List<InfoPratique> infoPratiques;
	
	private List<DangerPratique> dangerPratiques;

	
	public String getItineraire() {
		return itineraire;
	}

	public void setItineraire(String itineraire) {
		this.itineraire = itineraire;
	}

	public List<InfoPratique> getInfoPratiques() {
		return infoPratiques;
	}

	public void setInfoPratiques(List<InfoPratique> infoPratiques) {
		this.infoPratiques = infoPratiques;
	}

	public List<DangerPratique> getDangerPratiques() {
		return dangerPratiques;
	}

	public void setDangerPratiques(List<DangerPratique> dangerPratiques) {
		this.dangerPratiques = dangerPratiques;
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

	public double getStepOverLng() {
		return stepOverLng;
	}

	public void setStepOverLng(double stepOverLng) {
		this.stepOverLng = stepOverLng;
	}

	public double getStepOverLat() {
		return stepOverLat;
	}

	public void setStepOverLat(double stepOverLat) {
		this.stepOverLat = stepOverLat;
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
