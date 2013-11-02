package com.nomade.repository;

import com.nomade.domain.UserNomade;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = UserNomade.class)
public interface UserRepository {

    List<com.nomade.domain.UserNomade> findAll();
    List<com.nomade.domain.UserNomade> findByUserName(String userName);
    List<com.nomade.domain.UserNomade> findByCompteEmail(String email);
    List<com.nomade.domain.UserNomade> findById(String id);
}
