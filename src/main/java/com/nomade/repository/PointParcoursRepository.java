package com.nomade.repository;

import com.nomade.domain.PointPacours;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = PointPacours.class)
public interface PointParcoursRepository {

    List<com.nomade.domain.PointPacours> findAll();
}
