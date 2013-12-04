package com.nomade.repository;

import com.nomade.domain.DangerPratique;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = DangerPratique.class)
public interface DangerPratiqueRepository {

    List<com.nomade.domain.DangerPratique> findAll();
}
