// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.repository;

import com.nomade.domain.EtapeVoyage;
import com.nomade.repository.EtapeVoyageRepository;
import java.math.BigInteger;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

privileged aspect EtapeVoyageRepository_Roo_Mongo_Repository {
    
    declare parents: EtapeVoyageRepository extends PagingAndSortingRepository<EtapeVoyage, BigInteger>;
    
    declare @type: EtapeVoyageRepository: @Repository;
    
}
