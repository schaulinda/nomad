// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.FriendState;
import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;

privileged aspect Relation_Roo_JavaBean {
    
    public UserNomade Relation.getNomade() {
        return this.nomade;
    }
    
    public void Relation.setNomade(UserNomade nomade) {
        this.nomade = nomade;
    }
    
    public UserNomade Relation.getNomade2() {
        return this.nomade2;
    }
    
    public void Relation.setNomade2(UserNomade nomade2) {
        this.nomade2 = nomade2;
    }
    
    public FriendState Relation.getFriendState() {
        return this.friendState;
    }
    
    public void Relation.setFriendState(FriendState friendState) {
        this.friendState = friendState;
    }
    
}
