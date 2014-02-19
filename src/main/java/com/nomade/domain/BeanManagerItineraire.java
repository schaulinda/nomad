package com.nomade.domain;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class BeanManagerItineraire {
	
	private String start;
	private String end;
	
	private double startLng;
	private double startLat;
	
	private double endLng;
	private double endLat;
	
	private String itineraire;
	
	private String bol=null;
	
	private List<InfoPratique> infoPratiques;
	
	private List<DangerPratique> dangerPratiques;
	
	private List<InfoPratique> infoPratiquesAll;
	
	private List<DangerPratique> dangerPratiquesAll;

	private String makers;
	
	private String linkBase(HttpServletRequest httpServletRequest){
		
		StringBuilder stringBuilder = new StringBuilder()
		.append("http://")
		.append(httpServletRequest.getServerName())
		.append(":").append(httpServletRequest.getServerPort())
		.append("/resources/img")
		.append("/mapicon/");
		
		return stringBuilder.toString();
		
	}
	
	public void buildMakers(HttpServletRequest httpServletRequest){
		List<Marker> listMarkers = new ArrayList<Marker>();
		Marker mark = null;
		for(InfoPratique info:this.infoPratiquesAll){
			 mark = new Marker(info.getLocation(), info.getTitle());
			 mark.setTag("info");
				mark.setId(info.getId().toString());
				
				String linkBase = linkBase(httpServletRequest);
				String linkIcon=linkBase+info.getIcon();
				
				mark.getOptions().setIcon(linkIcon);
			listMarkers.add(mark);
		}
		for(DangerPratique danger:this.dangerPratiquesAll){
			 mark = new Marker(danger.getLocation(), danger.getTitle());
			 mark.setTag("danger");
			 mark.setId(danger.getId().toString());
				
				String linkBase = linkBase(httpServletRequest);
				String linkIcon=linkBase+danger.getIcon();
				
				mark.getOptions().setIcon(linkIcon);
			listMarkers.add(mark);
		}
		this.makers = Marker.toJsonArray(listMarkers);
		System.out.print(makers);
		
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

	public String getBol() {
		return bol;
	}

	public void setBol(String bol) {
		this.bol = bol;
	}

	public String getMakers() {
		return makers;
	}

	public void setMakers(String makers) {
		this.makers = makers;
	}

	public List<InfoPratique> getInfoPratiquesAll() {
		return infoPratiquesAll;
	}

	public void setInfoPratiquesAll(List<InfoPratique> infoPratiquesAll) {
		this.infoPratiquesAll = infoPratiquesAll;
	}

	public List<DangerPratique> getDangerPratiquesAll() {
		return dangerPratiquesAll;
	}

	public void setDangerPratiquesAll(List<DangerPratique> dangerPratiquesAll) {
		this.dangerPratiquesAll = dangerPratiquesAll;
	}

}
