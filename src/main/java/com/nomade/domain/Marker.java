package com.nomade.domain;

import org.springframework.roo.addon.json.RooJson;

@RooJson
public class Marker {
	
	private String address;
	
	private String data;

	public Marker(String address, String data) {
		super();
		this.address = address;
		this.data = data;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	

}
