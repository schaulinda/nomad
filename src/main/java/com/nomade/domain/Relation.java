package com.nomade.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class Relation {
	
	@DBRef
	private UserNomade nomade;
	
	@DBRef
	private UserNomade nomade2;
	
	private FriendState friendState;

	public Relation(UserNomade nomade, UserNomade nomade2,
			FriendState friendState) {
		super();
		this.nomade = nomade;
		this.nomade2 = nomade2;
		this.friendState = friendState;
	}

	public Relation() {
		super();
	}
	
	

}
