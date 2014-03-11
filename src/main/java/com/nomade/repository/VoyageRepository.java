package com.nomade.repository;

import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Voyage.class)
public interface VoyageRepository {

    List<com.nomade.domain.Voyage> findAll();
    List<Voyage> findByNomade(UserNomade nomad);
    List<Voyage> findByNomadeAndStatus(UserNomade nomad, StatusVoyage status);
}
