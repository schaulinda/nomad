package com.nomade.domain;

import org.springframework.roo.addon.json.RooJson;

@RooJson
public class Marker {
	
	private String id;
	
	private String tag;
	
	private String address;
	
	private String data;
	
	private Options options = new Options();

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
	

}
