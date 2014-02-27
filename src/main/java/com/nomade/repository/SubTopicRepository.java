package com.nomade.repository;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

import com.nomade.domain.Confidentiality;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.tools.ForumQuery;

@RooMongoRepository(domainType = SubTopic.class)
public interface SubTopicRepository  extends ForumQuery<SubTopic> {

    List<SubTopic> findAll();
    public List<SubTopic> findByParentTopic(Topic topic);
    public List<SubTopic> findByParentTopic(Topic topic,Pageable pageable);
    public List<SubTopic> findByParentTopicAndConfidentiality(Topic topic,Confidentiality confidentiality);
    public List<SubTopic> findByParentTopicAndConfidentiality(Topic topic,Confidentiality confidentiality,Pageable pageable);
}
