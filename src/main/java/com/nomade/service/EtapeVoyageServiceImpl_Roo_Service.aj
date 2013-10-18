// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.service;

import com.nomade.domain.EtapeVoyage;
import com.nomade.repository.EtapeVoyageRepository;
import com.nomade.service.EtapeVoyageServiceImpl;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EtapeVoyageServiceImpl_Roo_Service {
    
    declare @type: EtapeVoyageServiceImpl: @Service;
    
    declare @type: EtapeVoyageServiceImpl: @Transactional;
    
    @Autowired
    EtapeVoyageRepository EtapeVoyageServiceImpl.etapeVoyageRepository;
    
    public long EtapeVoyageServiceImpl.countAllEtapeVoyages() {
        return etapeVoyageRepository.count();
    }
    
    public void EtapeVoyageServiceImpl.deleteEtapeVoyage(EtapeVoyage etapeVoyage) {
        etapeVoyageRepository.delete(etapeVoyage);
    }
    
    public EtapeVoyage EtapeVoyageServiceImpl.findEtapeVoyage(BigInteger id) {
        return etapeVoyageRepository.findOne(id);
    }
    
    public List<EtapeVoyage> EtapeVoyageServiceImpl.findAllEtapeVoyages() {
        return etapeVoyageRepository.findAll();
    }
    
    public List<EtapeVoyage> EtapeVoyageServiceImpl.findEtapeVoyageEntries(int firstResult, int maxResults) {
        return etapeVoyageRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void EtapeVoyageServiceImpl.saveEtapeVoyage(EtapeVoyage etapeVoyage) {
        etapeVoyageRepository.save(etapeVoyage);
    }
    
    public EtapeVoyage EtapeVoyageServiceImpl.updateEtapeVoyage(EtapeVoyage etapeVoyage) {
        return etapeVoyageRepository.save(etapeVoyage);
    }
    
}
