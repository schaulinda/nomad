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
public class EtapeVoyage {
	
	@GeoSpatialIndexed
	private double[] geolocation;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dateEtape;

    private String userPhoto;

    private String location;
    
    private double userlat;
    
    private double userlng;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date created = new Date();
    
    @DBRef
	private UserNomade nomade;
    
    @OrderBy(value = "created", order=Order.DESCENDING)
    private List<Comment> comments = new ArrayList<Comment>();
    
    @DBRef
    private Voyage voyage;
    
}
