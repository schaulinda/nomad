// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.PointPacours;
import java.math.BigInteger;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;

privileged aspect PointPacours_Roo_Mongo_Entity {
    
    declare @type: PointPacours: @Persistent;
    
    @Id
    private BigInteger PointPacours.id;
    
    public BigInteger PointPacours.getId() {
        return this.id;
    }
    
    public void PointPacours.setId(BigInteger id) {
        this.id = id;
    }
    
}
