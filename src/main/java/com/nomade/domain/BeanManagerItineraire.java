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
		.append("/mapicon");
		
		return stringBuilder.toString();
		
	}
	
	public void buildMakers(HttpServletRequest httpServletRequest){
		List<Marker> listMarkers = new ArrayList<Marker>();
		Marker mark = null;
		for(InfoPratique info:this.infoPratiquesAll){
			 mark = new Marker(info.getLocation(), info.getTitle());
			 mark.setTag("info");
				mark.setId(info.getId().toString());
				String typeInfo = info.getSelecteur1();
				/*"pointEau","toilettes","douches"
				"stationnementGratuite","stationnementPayant","jardin","camping","pointRencontre"
				station","vidange","shipping","rassemblement","garage
				faune","flore","géologique","historique, monuments","marins","lac","plage","randonnée"*/
				String linkBase = linkBase(httpServletRequest);
				String linkIcon=linkBase+"/info";
				if(typeInfo.equals("toilettes")){
					linkIcon = linkIcon+"/toillete.png";
				}
				if(typeInfo.equals("pointEau")){
					linkIcon = linkIcon+"/pointEau.png";
				}
				if(typeInfo.equals("faune")){
					linkIcon = linkIcon+"/curiositeFone.png";
				}
				if(typeInfo.equals("douches")){
					linkIcon = linkIcon+"/douche.png";
				}
				if(typeInfo.equals("stationnementGratuite")){
					linkIcon = linkIcon+"/airStationementGratuit.png";
				}
				if(typeInfo.equals("stationnementPayant")){
					linkIcon = linkIcon+"/aireStationementPayant.png";
				}
				if(typeInfo.equals("jardin")){
					linkIcon = linkIcon+"/plage.png";//to be change with the real jardin icon 
				}
				if(typeInfo.equals("camping")){
					linkIcon = linkIcon+"/reseauNomad.png";
				}
				if(typeInfo.equals("pointRencontre")){
					linkIcon = linkIcon+"/pointRencontre.png";
				}
				if(typeInfo.equals("station")){
					linkIcon = linkIcon+"/stationEssence.png";
				}
				if(typeInfo.equals("vidange")){
					linkIcon = linkIcon+"/vidangeCampingCar.png";
				}
				if(typeInfo.equals("shipping")){
					linkIcon = linkIcon+"/shipping.png";
				}
				if(typeInfo.equals("rassemblement")){
					linkIcon = linkIcon+"/rassemblement.png";
				}
				if(typeInfo.equals("garage")){
					linkIcon = linkIcon+"/garage.png";
				}
				if(typeInfo.equals("flore")){
					linkIcon = linkIcon+"/curiositeFlore.png";
				}
				if(typeInfo.equals("geologique")){
					linkIcon = linkIcon+"/curiositeGeologiaue.png";
				}
				if(typeInfo.equals("historique")){
					linkIcon = linkIcon+"/historique.png";
				}
				if(typeInfo.equals("monuments")){
					linkIcon = linkIcon+"/curiositeMonument.png";
				}
				if(typeInfo.equals("marins")){
					linkIcon = linkIcon+"/curiositeFond.png";
				}
				if(typeInfo.equals("lac")){
					linkIcon = linkIcon+"/curiositeLac.png";
				}
				if(typeInfo.equals("plage")){
					linkIcon = linkIcon+"/plage.png";
				}
				if(typeInfo.equals("randonnee")){
					linkIcon = linkIcon+"/departRandonne.png";
				}
				mark.getOptions().setIcon(linkIcon);
			listMarkers.add(mark);
		}
		for(DangerPratique danger:this.dangerPratiquesAll){
			 mark = new Marker(danger.getLocation(), danger.getTitle());
			 mark.setTag("danger");
			 mark.setId(danger.getId().toString());
				String typeInfo = danger.getSelecteur1();
				/*["impraticable","chausse","piste","bande","boue","route","denivelation",
                 "effondrement","inondations","poids","hauteur","travaux"]
                 
                 var administratifValue = ["douane","information","immigration","barrage","greve"]; 
                 var risqueValue = ["agressions","agressionMain","escroquerie"];  
                 */
				String linkBase = linkBase(httpServletRequest);
				String linkIcon=linkBase+"/danger";
				if(typeInfo.equals("impraticable")){
					linkIcon = linkIcon+"/impraticable.png";
				}
				if(typeInfo.equals("chausse")){
					linkIcon = linkIcon+"/chausse.png";
				}
				if(typeInfo.equals("piste")){
					linkIcon = linkIcon+"/piste.png";
				}
				if(typeInfo.equals("bande")){
					linkIcon = linkIcon+"/bande.png";
				}
				if(typeInfo.equals("boue")){
					linkIcon = linkIcon+"/boue.png";
				}
				if(typeInfo.equals("route")){
					linkIcon = linkIcon+"/route.png";
				}
				if(typeInfo.equals("denivelation")){
					linkIcon = linkIcon+"/denivelation.png";
				}
				if(typeInfo.equals("effondrement")){
					linkIcon = linkIcon+"/effondrement.png";
				}
				if(typeInfo.equals("inondations")){
					linkIcon = linkIcon+"/inondations.png";
				}
				if(typeInfo.equals("poids")){
					linkIcon = linkIcon+"/poids.png";
				}
				if(typeInfo.equals("hauteur")){
					linkIcon = linkIcon+"/hauteur.png";
				}
				if(typeInfo.equals("travaux")){
					linkIcon = linkIcon+"/travaux.png";
				}
				if(typeInfo.equals("douane")){
					linkIcon = linkIcon+"/douane.png";
				}
				if(typeInfo.equals("information")){
					linkIcon = linkIcon+"/information.png";
				}
				if(typeInfo.equals("immigration")){
					linkIcon = linkIcon+"/immigration.png";
				}
				if(typeInfo.equals("barrage")){
					linkIcon = linkIcon+"/barrage.png";
				}
				if(typeInfo.equals("greve")){
					linkIcon = linkIcon+"/greve.png";
				}
				if(typeInfo.equals("agressions")){
					linkIcon = linkIcon+"/agressions.png";
				}
				if(typeInfo.equals("agressionMain")){
					linkIcon = linkIcon+"/agressionMain.png";
				}
				if(typeInfo.equals("escroquerie")){
					linkIcon = linkIcon+"/escroquerie.png";
				}
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
