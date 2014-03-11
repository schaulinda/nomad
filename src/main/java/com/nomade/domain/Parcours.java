package com.nomade.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

import com.nomade.plugin.OrderBy;

@RooJavaBean
@RooToString
@RooMongoEntity
@RooJson
public class Parcours {
	
	private Etape depart;
	private Etape arrived;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date created = new Date();
	
	private int nbreEtape = 0;
	
	@DBRef
	private UserNomade nomad;
	
	 @DBRef
	 private Voyage voyage;
	 
	 @OrderBy(value = "day", order=Order.ASCENDING)
	 private List<Etape> etapes = new ArrayList<Etape>();
		
		

}
