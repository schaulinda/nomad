package com.nomade.repository;
import java.util.List;

import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.tools.ForumQuery;

@RooMongoRepository(domainType = SubTopic.class)
public interface SubTopicRepository  extends ForumQuery<Discussion> {

    List<SubTopic> findAll();
    public List<SubTopic> findByParentTopic(Topic topic);
}