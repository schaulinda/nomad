package com.nomade.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.StatusVoyage;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

@RooMongoRepository(domainType = Voyage.class)
public interface VoyageRepository {

    List<com.nomade.domain.Voyage> findAll();
    List<Voyage> findByNomade(UserNomade nomad);
    List<Voyage> findByNomadeAndStatus(UserNomade nomad, StatusVoyage status);
    Page<Voyage> findByNomade(UserNomade nomade, Pageable pageable);
}
