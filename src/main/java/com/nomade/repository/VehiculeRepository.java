package com.nomade.repository;

import com.nomade.domain.Vehicule;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Vehicule.class)
public interface VehiculeRepository {

    List<com.nomade.domain.Vehicule> findAll();
}
