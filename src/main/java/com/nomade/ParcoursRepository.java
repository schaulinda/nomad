package com.nomade;

import com.nomade.domain.Parcours;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Parcours.class)
public interface ParcoursRepository {

    List<com.nomade.domain.Parcours> findAll();
}
