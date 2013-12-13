package com.nomade.repository;

import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.UserNomade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = EtapeVehicule.class)
public interface EtapeVehiculeRepository {

    List<com.nomade.domain.EtapeVehicule> findAll();
    Page<EtapeVehicule> findByNomade(UserNomade nomade, Pageable pageable);
  

}
