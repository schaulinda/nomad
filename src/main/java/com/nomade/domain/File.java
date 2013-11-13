package com.nomade.domain;

import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class File {

    @NotNull
    private String fileName;
    
    private String fileSize;
    private String fileType;
 
    
    private double[] location;
}
