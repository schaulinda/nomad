package com.nomade.repository;
import com.nomade.domain.SubTopic;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = SubTopic.class)
public interface SubTopicRepository {

    List<SubTopic> findAll();
}
