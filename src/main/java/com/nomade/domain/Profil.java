package com.nomade.domain;

import java.util.HashSet;
import java.util.Set;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.multipart.MultipartFile;

@RooJavaBean
@RooToString
@RooMongoEntity
public class Profil {

    private String pseudo;

    private String website;
    
    private String file;

    private Set<Langue> langues = new HashSet<Langue>();

 
    private Set<Country> visitedCountry = new HashSet<Country>();

    private String butVoyage;

    private String metier;

	public Profil(String pseudo, String website, Set<Langue> langues,
			Set<Country> visitedCountry, String butVoyage, String metier) {
		super();
		this.pseudo = pseudo;
		this.website = website;
		this.langues = langues;
		this.visitedCountry = visitedCountry;
		this.butVoyage = butVoyage;
		this.metier = metier;
	}

	public Profil() {
		super();
	}
    
    
}
