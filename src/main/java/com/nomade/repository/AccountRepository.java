package com.nomade.repository;

import com.nomade.domain.Account;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Account.class)
public interface AccountRepository {

    List<com.nomade.domain.Account> findAll();
}
