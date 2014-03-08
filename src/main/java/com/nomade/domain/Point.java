package com.nomade.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooMongoEntity
@RooJson
public class Point {
	
	private Etape point;

}
