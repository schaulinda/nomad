package com.nomade.repository;

import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = EtapeVoyage.class)
public interface EtapeVoyageRepository {

    List<com.nomade.domain.EtapeVoyage> findAll();
    
    Page<EtapeVoyage> findByNomade(UserNomade nomade, Pageable pageable);
}
