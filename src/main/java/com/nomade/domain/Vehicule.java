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
    private VehiculeType vehiculeType = VehiculeType.Autre;

    @Enumerated
    private VehiculeState vehiculeState = VehiculeState.withoutvehicle;

    private String model;

    private String couleur;

    @Enumerated
    private Country forSellCountry;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date anneMiseEnService;

    private String description;
    
    private String icon;
    
    /*withoutvehicle, lookingForBuy, lookingForSale, onTheRoad, stopOver, inRepair, backToHome;*/
    /*DeuxRouesRoutier, DeuxRouesToutTerrain, QuatrexQuatre, CampingCar, Camion, Camionette, Velo, Voiture, Autre;*/
    private void makeIcon(){
    	
    	this.icon = ""+this.vehiculeType+""+this.vehiculeState;
    }
    
    public void setVehiculeType(VehiculeType vehiculeType) {
        this.vehiculeType = vehiculeType;
        makeIcon();
    }
    
    public void setVehiculeState(VehiculeState vehiculeState) {
        this.vehiculeState = vehiculeState;
        makeIcon();
    }
}
