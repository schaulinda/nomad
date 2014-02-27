package com.nomade.repository;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.Confidentiality;
import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.tools.ForumQuery;

@RooMongoRepository(domainType = Discussion.class)
public interface DiscussionRepository extends ForumQuery<Discussion>{

    List<Discussion> findAll();
    public List<Discussion> findBySubTopic(SubTopic subTopic);
    public List<Discussion> findBySubTopic(SubTopic subTopic,Pageable pageable);
    public List<Discussion> findBySubTopicAndConfidentiality(SubTopic topic,Confidentiality confidentiality);
    public List<Discussion> findBySubTopicAndConfidentiality(SubTopic topic,Confidentiality confidentiality,Pageable pageable);
}
