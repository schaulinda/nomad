package com.nomade.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.mongodb.DBObject;

@RooJavaBean
@RooToString
@RooJson
public class ImageInfo {
	
    private double[] location;
    
    private String adress;
    
    private Date datePhoto;
    
    private String description;
    
    private String albumId;
    
    private double lat;
    
    private double lng;

	public ImageInfo(String adress, Date datePhoto, String description,
			String albumId, double x, double y) {
		super();
		this.adress = adress;
		this.datePhoto = datePhoto;
		this.description = description;
		this.albumId = albumId;
		this.location = new double[]{x,y};
		this.lat = x;
		this.lng = y;
	}

	public ImageInfo() {
		super();
	}
	
	
	
	public ImageInfo(DBObject metaData) {
		super();
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			this.adress = metaData.get("adress").toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			this.adress="";
		}
		
			try {
				this.datePhoto =(Date)metaData.get("datePhoto");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				
			}
		
		try {
			this.description = metaData.get("description").toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			this.description="";
		}
		
		this.albumId = metaData.get("albumId").toString();
		
		try {
			this.lat = Double.parseDouble(metaData.get("lat").toString());
			this.lng = Double.parseDouble(metaData.get("lng").toString());
		} catch (NumberFormatException e) {
			this.lat=0;
			this.lng=0;
		}
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
    
	
}
