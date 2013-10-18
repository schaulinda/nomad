package com.nomade.repository;

import com.nomade.domain.Profil;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Profil.class)
public interface ProfilRepository {

    List<com.nomade.domain.Profil> findAll();
}
