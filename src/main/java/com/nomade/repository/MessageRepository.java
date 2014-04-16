package com.nomade.repository;

import com.nomade.domain.Message;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Message.class)
public interface MessageRepository {

    List<com.nomade.domain.Message> findAll();
}
