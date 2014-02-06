package com.nomade.repository;
import com.nomade.domain.Discussion;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Discussion.class)
public interface DiscussionRepository {

    List<Discussion> findAll();
}
