// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.Etape;
import com.nomade.domain.UserNomade;
import java.util.Date;

privileged aspect Etape_Roo_JavaBean {
    
    public String Etape.getLocation() {
        return this.location;
    }
    
    public void Etape.setLocation(String location) {
        this.location = location;
    }
    
    public double Etape.getLat() {
        return this.lat;
    }
    
    public double Etape.getLng() {
        return this.lng;
    }
    
    public double[] Etape.getCoord() {
        return this.coord;
    }
    
    public void Etape.setCoord(double[] coord) {
        this.coord = coord;
    }
    
    public Date Etape.getDay() {
        return this.day;
    }
    
    public void Etape.setDay(Date day) {
        this.day = day;
    }
    
    public UserNomade Etape.getNomad() {
        return this.nomad;
    }
    
    public void Etape.setNomad(UserNomade nomad) {
        this.nomad = nomad;
    }
    
}