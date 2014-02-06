package com.nomade.repository;
import java.util.List;

import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;

@RooMongoRepository(domainType = SubTopic.class)
public interface SubTopicRepository {

    List<SubTopic> findAll();
    public List<SubTopic> findByParentTopic(Topic topic);
}
