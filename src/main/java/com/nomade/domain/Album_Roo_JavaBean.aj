// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.Album;
import com.nomade.domain.File;
import java.util.Set;

privileged aspect Album_Roo_JavaBean {
    
    public String Album.getName() {
        return this.name;
    }
    
    public void Album.setName(String name) {
        this.name = name;
    }
    
    public Set<File> Album.getFiles() {
        return this.files;
    }
    
    public void Album.setFiles(Set<File> files) {
        this.files = files;
    }
    
}
