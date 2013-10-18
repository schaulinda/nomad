// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.service;

import com.nomade.domain.Vehicule;
import com.nomade.service.VehiculeService;
import java.math.BigInteger;
import java.util.List;

privileged aspect VehiculeService_Roo_Service {
    
    public abstract long VehiculeService.countAllVehicules();    
    public abstract void VehiculeService.deleteVehicule(Vehicule vehicule);    
    public abstract Vehicule VehiculeService.findVehicule(BigInteger id);    
    public abstract List<Vehicule> VehiculeService.findAllVehicules();    
    public abstract List<Vehicule> VehiculeService.findVehiculeEntries(int firstResult, int maxResults);    
    public abstract void VehiculeService.saveVehicule(Vehicule vehicule);    
    public abstract Vehicule VehiculeService.updateVehicule(Vehicule vehicule);    
}
