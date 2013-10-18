package com.nomade.domain;

import java.util.Date;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class Vehicule {

    private String vehiculeName;

    private String photo;

    @Enumerated
    private VehiculeType vehiculeType;

    @Enumerated
    private VehiculeState vehiculeState;

    private String model;

    private String couleur;

    @Enumerated
    private Country forSellCountry;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date anneMiseEnService;

    private String description;
}
