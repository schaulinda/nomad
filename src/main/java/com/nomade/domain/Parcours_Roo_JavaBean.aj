// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.Etape;
import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import java.util.Date;

privileged aspect Parcours_Roo_JavaBean {
    
    public Etape Parcours.getDepart() {
        return this.depart;
    }
    
    public void Parcours.setDepart(Etape depart) {
        this.depart = depart;
    }
    
    public Etape Parcours.getArrived() {
        return this.arrived;
    }
    
    public void Parcours.setArrived(Etape arrived) {
        this.arrived = arrived;
    }
    
    public Date Parcours.getCreated() {
        return this.created;
    }
    
    public void Parcours.setCreated(Date created) {
        this.created = created;
    }
    
    public UserNomade Parcours.getNomad() {
        return this.nomad;
    }
    
    public void Parcours.setNomad(UserNomade nomad) {
        this.nomad = nomad;
    }
    
    public Voyage Parcours.getVoyage() {
        return this.voyage;
    }
    
    public void Parcours.setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }
    
}
