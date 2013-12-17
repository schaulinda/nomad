package com.nomade.domain;

import org.springframework.roo.addon.json.RooJson;

@RooJson
public class Options {
	
	private String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
