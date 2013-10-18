package com.nomade.repository;

import com.nomade.domain.EtapeVehicule;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = EtapeVehicule.class)
public interface EtapeVehiculeRepository {

    List<com.nomade.domain.EtapeVehicule> findAll();
}
