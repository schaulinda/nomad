package com.nomade.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class FileMsg {
	
	private UserNomade nomad1;
	
	private UserNomade nomad2;
	
	private List<Message> messages = new ArrayList<Message>();
	
	private int numberUnreadMsg = 0;
	

}
