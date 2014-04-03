package com.nomade.domain;

import java.util.List;


public class BeanNomadeManager {
	
	private UserNomade nomade;
		
	private boolean amie;
	
	private boolean me;
	
	private boolean home;

	
	private List<Marker> marker;
	
	
	public UserNomade getNomade() {
		return nomade;
	}

	public void setNomade(UserNomade nomade) {
		this.nomade = nomade;
	}

	public boolean isAmie() {
		return amie;
	}

	public void setAmie(boolean amie) {
		this.amie = amie;
	}

	public boolean isMe() {
		return me;
	}

	public void setMe(boolean me) {
		this.me = me;
	}


	public boolean isHome() {
		return home;
	}

	public void setHome(boolean home) {
		this.home = home;
	}

	public List<Marker> getMarker() {
		return marker;
	}

	public void setMarker(List<Marker> marker) {
		this.marker = marker;
	}

	
}
