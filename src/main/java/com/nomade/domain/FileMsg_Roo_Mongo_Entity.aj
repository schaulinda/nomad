// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.FileMsg;
import java.math.BigInteger;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;

privileged aspect FileMsg_Roo_Mongo_Entity {
    
    declare @type: FileMsg: @Persistent;
    
    @Id
    private BigInteger FileMsg.id;
    
    public BigInteger FileMsg.getId() {
        return this.id;
    }
    
    public void FileMsg.setId(BigInteger id) {
        this.id = id;
    }
    
}
