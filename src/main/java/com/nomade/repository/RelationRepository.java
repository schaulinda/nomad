package com.nomade.repository;

import com.nomade.domain.Relation;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Relation.class)
public interface RelationRepository {

    List<com.nomade.domain.Relation> findAll();
}
