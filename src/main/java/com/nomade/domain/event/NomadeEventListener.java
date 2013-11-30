package com.nomade.domain.event;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

import com.mongodb.DBObject;
import com.nomade.domain.UserNomade;

public class NomadeEventListener extends AbstractMongoEventListener<UserNomade> {
	
	
	@Override
	public void onBeforeSave(UserNomade source, DBObject dbo) {
		
		
		//Collections.reverse(source.getAlbums().);
		super.onBeforeSave(source, dbo);
	}
	
	
	

}
