// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.Discussion;
import java.math.BigInteger;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;

privileged aspect Discussion_Roo_Mongo_Entity {
    
    declare @type: Discussion: @Persistent;
    
    @Id
    private BigInteger Discussion.id;
    
    public BigInteger Discussion.getId() {
        return this.id;
    }
    
    public void Discussion.setId(BigInteger id) {
        this.id = id;
    }
    
}