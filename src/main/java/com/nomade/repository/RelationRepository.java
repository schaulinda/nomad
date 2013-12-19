package com.nomade.repository;

import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;

import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;
import org.springframework.security.core.userdetails.User;

@RooMongoRepository(domainType = Relation.class)
public interface RelationRepository {

    List<com.nomade.domain.Relation> findAll();
    
    List<Relation> findByNomadeAndNomade2(UserNomade nomade, UserNomade nomade2);
    
    List<Relation> findByNomade2AndNomade(UserNomade nomade, UserNomade nomade2);
    
    List<Relation> findByNomade(UserNomade nomade);
    
    List<Relation> findByNomadeOrNomade2(UserNomade nomade,UserNomade nomade1);
}
