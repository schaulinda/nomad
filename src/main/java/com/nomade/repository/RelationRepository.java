package com.nomade.repository;

import com.nomade.domain.FriendState;
import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;

import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Relation.class)
public interface RelationRepository {

    List<com.nomade.domain.Relation> findAll();

    
    List<Relation> findByNomadeAndNomade2(UserNomade nomade, UserNomade nomade2);
    
    List<Relation> findByNomadeAndNomade2OrNomade2AndNomade(UserNomade nomade, UserNomade nomade2, UserNomade nomade3, UserNomade nomade4);
    
    List<Relation> findByNomade2AndNomade(UserNomade nomade, UserNomade nomade2);
    
    List<Relation> findByNomade(UserNomade nomade);
    
    List<Relation> findByNomade2AndFriendState(UserNomade nomade, FriendState friendState);//mes demande en attende
    
    List<Relation> findByFriendStateAndNomadeOrFriendStateAndNomade2(FriendState friendState, 
    		UserNomade nomade, FriendState friendState1, UserNomade nomade1);//mes amies
    
    List<Relation> findByNomadeOrNomade2AndFriendState(UserNomade nomade,UserNomade nomade2,FriendState friendState);
    
    List<Relation> findByNomadeOrNomade2(UserNomade nomade,UserNomade nomade1);
}
