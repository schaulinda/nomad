package com.nomade.repository;

import com.nomade.domain.FileMsg;
import com.nomade.domain.UserNomade;

import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = FileMsg.class)
public interface FileMsgRepository {

    List<com.nomade.domain.FileMsg> findAll();
    List<FileMsg> findByNomad1AndNomad2OrNomad2AndNomad1(UserNomade nomade, UserNomade nomade2, UserNomade nomade3, UserNomade nomade4);
    List<FileMsg> findByNomad1OrNomad2(UserNomade nomade, UserNomade nomade2);
}
