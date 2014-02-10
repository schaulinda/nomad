package com.nomade.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
@RooJson
public class Etape {
	
	private String location;
	private double lat;
	private double lng;
	private double[] coord;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date dateEtape = new Date();
	private UserNomade nomad;

	
	 public void setLat(double lat) {
	        this.lat = lat;
	    }
	 
	 public void setDateEtape(Date dateEtape) {
	        this.dateEtape = dateEtape;
	    }
	 
	 public void setLng(double lng) {
	        this.lng = lng;
	    }
	 
	 public void setDateEtape(String dateEtape) {
		
		
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		 	Date date;
			try {
				date = sdf.parse(dateEtape);
				this.dateEtape = date;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.dateEtape= new Date();
			}	

	    
   }
}

