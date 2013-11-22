package com.nomade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import com.nomade.domain.Album;

public class AlbumServiceImpl implements AlbumService {

	@Autowired
	MongoTemplate mongoTemplate;

	public String albumName(String idAlbum) {

		 Criteria c1 = Criteria.where("userName").is(null);
	        // the field object
	        Criteria c2 = Criteria.where("albums").elemMatch(Criteria.where("_id").is(idAlbum));
	        BasicQuery query = new BasicQuery(c1.getCriteriaObject(), c2.getCriteriaObject());
	 
		return mongoTemplate.findOne(query, Album.class).getName();
	}
}
