package com.nomade.repository;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Topic;

import java.util.List;

import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Topic.class)
public interface TopicRepository {

    List<Topic> findAll();
    List<Topic> findByConfidentiality(Confidentiality confidentiality);
}
