package com.nomade.repository;
import java.util.List;

import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.tools.ForumQuery;

@RooMongoRepository(domainType = Discussion.class)
public interface DiscussionRepository extends ForumQuery<Discussion>{

    List<Discussion> findAll();
    public List<Discussion> findBySubTopic(SubTopic subTopic);
}
