package com.nomade.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooMongoEntity
@RooJson
public class PointPacours {
	
	private Etape point;
	
	@DBRef
	private Parcours parcours;

}
