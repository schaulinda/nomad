package com.nomade.repository;

import com.nomade.domain.File;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = File.class)
public interface FileRepository {

    List<com.nomade.domain.File> findAll();
}
