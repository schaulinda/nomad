package com.nomade.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class Album {
	
	private ObjectId _id = new ObjectId();

    @NotNull
    private String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date created = new Date();
    
    private int numPhoto = 0;
    
    private String[] photos;

	public String[] getPhotos() {
		return photos;
	}

	public void setPhotos(String[] photos) {
		this.photos = photos;
	}

	public Album(String name, Date created) {
		super();
		this.name = name;
		this.created = created;
		
	}
	
	

	public Album(ObjectId _id, String name, Date created, int numPhoto,
			String[] photos) {
		super();
		this._id = _id;
		this.name = name;
		this.created = created;
		this.numPhoto = numPhoto;
		this.photos = photos;
	}

	public Album() {
		super();
		
	}

	public int getNumPhoto() {
		return numPhoto;
	}

	public void setNumPhoto(int numPhoto) {
		this.numPhoto = numPhoto;
	}
    

	
	
}
