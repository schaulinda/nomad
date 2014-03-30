package com.nomade.repository;

import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = InfoPratique.class)
public interface InfoPratiqueRepository {

    List<com.nomade.domain.InfoPratique> findAll();
    Page<InfoPratique> findByNomade(UserNomade nomade, Pageable pageable);
    
    List<InfoPratique> findByNomadeOrderByCreatedDesc(UserNomade nomade);
}
