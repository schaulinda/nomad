package com.nomade.repository;

import com.nomade.domain.EtapeVoyage;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = EtapeVoyage.class)
public interface EtapeVoyageRepository {

    List<com.nomade.domain.EtapeVoyage> findAll();
}
