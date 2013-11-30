package com.nomade.repository;

import com.nomade.domain.InfoPratique;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = InfoPratique.class)
public interface InfoPratiqueRepository {

    List<com.nomade.domain.InfoPratique> findAll();
}
