package com.nomade.domain;

import java.util.List;


public class BeanNomadeManager {
	
	private UserNomade nomade;
	
	List<UserNomade> nomads;
	
	private boolean amie;
	
	private boolean me;
	
	private boolean home;
	
	private String makers;

	
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

	public boolean isHome() {
		return home;
	}

	public void setHome(boolean home) {
		this.home = home;
	}
	
}
