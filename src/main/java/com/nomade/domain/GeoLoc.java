package com.nomade.domain;

public class GeoLoc {
	
	private String type = "LineString";
	
	private double[][] coord;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double[][] getCoord() {
		return coord;
	}

	public void setCoord(double[][] coord) {
		this.coord = coord;
	}

}
