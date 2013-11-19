package com.nomade.repository;

import com.nomade.domain.ImageInfo;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = ImageInfo.class)
public interface FileRepository {

    List<com.nomade.domain.ImageInfo> findAll();
}
