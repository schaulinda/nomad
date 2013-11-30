package com.nomade.domain;

import java.util.List;

public class BeanManagerItineraire {
	
	private double[] start;
	
	private double[] stepOver;
	
	private double[] end;
	
	private String itineraire;
	
	private List<InfoPratique> infoPratiques;
	
	private List<DangerPratique> dangerPratiques;

	public double[] getStart() {
		return start;
	}

	public void setStart(double[] start) {
		this.start = start;
	}

	public double[] getStepOver() {
		return stepOver;
	}

	public void setStepOver(double[] stepOver) {
		this.stepOver = stepOver;
	}

	public double[] getEnd() {
		return end;
	}

	public void setEnd(double[] end) {
		this.end = end;
	}

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

}
