package com.nomade.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.UserNomade;

@RooMongoRepository(domainType = EtapeVehicule.class)
public interface EtapeVehiculeRepository {

    List<com.nomade.domain.EtapeVehicule> findAll();
    Page<EtapeVehicule> findByNomade(UserNomade nomade, Pageable pageable);
  

}
