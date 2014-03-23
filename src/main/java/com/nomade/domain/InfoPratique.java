package com.nomade.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

import com.nomade.plugin.OrderBy;

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
	
	private TypeTime dure;
	
	private boolean infoVerif;
	
	private TypeTime estimationValidite;
	
	private String comment;
	
	private int votePositif;
	
	private int voteNegatif;
	
	private List<UserNomade> listVotants = new ArrayList<UserNomade>();
	
	private String icon;
	
	 @OrderBy(value = "created", order=Order.DESCENDING)
	    private List<Comment> comments = new ArrayList<Comment>();
	
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<UserNomade> getListVotants() {
		return listVotants;
	}

	public void setListVotants(List<UserNomade> listVotants) {
		this.listVotants = listVotants;
	}

}
