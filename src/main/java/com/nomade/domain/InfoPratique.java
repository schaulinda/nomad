package com.nomade.domain;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class InfoPratique {
	
	private String title;
	
	private String selecteur;//to be change
	
	private String selecteur1;
	
	private String location;
	
	private double locationLat;
	
	private double locationLng;
	
	@GeoSpatialIndexed
	private double[] geoLocation;
	
	private String photo;
	
	private int dure;
	
	private boolean infoVerif;
	
	private int estimationValidite;
	
	private String comment;
	
	private int votePositif;
	
	private int voteNegatif;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date created = new Date();
	
	@DBRef
	private UserNomade nomade;
	
	
	public void incrementVotePositif(){
		
		this.votePositif=this.votePositif+1;
	}
	
	public void incrementVoteNegatif(){
		
		this.voteNegatif=this.voteNegatif+1;
	}

}
