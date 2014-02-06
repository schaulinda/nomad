package com.nomade.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.DangerPratique;
import com.nomade.domain.UserNomade;

@RooMongoRepository(domainType = DangerPratique.class)
public interface DangerPratiqueRepository {

    List<com.nomade.domain.DangerPratique> findAll();
    Page<DangerPratique> findByNomade(UserNomade nomade, Pageable pageable);
}
