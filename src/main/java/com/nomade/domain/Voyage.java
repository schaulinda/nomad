package com.nomade.domain;


import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class Voyage {
	
	private String title;
	private Etape depart;
	private Etape arrived;
	private StatusVoyage status;
	private boolean terminated;
	private int nbreParcours = 0;
	@DBRef
	private UserNomade nomade;

}
