package com.nomade.repository;

import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Voyage;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Parcours.class)
public interface ParcoursRepository {

    List<com.nomade.domain.Parcours> findAll();
    public List<Parcours> findByNomadOrderByCreatedDesc(UserNomade nomad);
    public List<Parcours> findByNomad(UserNomade nomad);
    public List<Parcours> findByVoyage(Voyage voyage);
    public List<Parcours> findByVoyage(Voyage voyage, Sort sort);
}
