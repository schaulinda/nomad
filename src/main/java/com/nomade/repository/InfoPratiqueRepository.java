package com.nomade.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;

@RooMongoRepository(domainType = InfoPratique.class)
public interface InfoPratiqueRepository {

    List<com.nomade.domain.InfoPratique> findAll();
    Page<InfoPratique> findByNomade(UserNomade nomade, Pageable pageable);
}
