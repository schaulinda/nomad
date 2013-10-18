package com.nomade.repository;

import com.nomade.domain.Album;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Album.class)
public interface AlbumRepository {

    List<com.nomade.domain.Album> findAll();
}
