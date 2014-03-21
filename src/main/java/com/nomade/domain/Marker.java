package com.nomade.domain;

import org.springframework.roo.addon.json.RooJson;

@RooJson
public class Marker {
	
	private String id;
	
	private String tag;
	
	private double[] latLng;
	
	private String data;
	
	private Options options = new Options();
	
	
	


	public Marker(double[] latLng, String data) {
		super();
		this.latLng = latLng;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public double[] getLatLng() {
		return latLng;
	}

	public void setLatLng(double[] latLng) {
		this.latLng = latLng;
	}
	

}
