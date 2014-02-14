package com.nomade.repository;

import com.nomade.domain.Etape;
import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;

import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Etape.class)
public interface EtapeRepository {

    List<com.nomade.domain.Etape> findAll();
    
    public List<Etape> findByNomadOrderByDayDesc(UserNomade nomad);
}
