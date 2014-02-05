package com.nomade.domain;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class SubTopic {

    /**
     */
    @NotNull
    private String title;

    /**
     * The label that describe this subtopic
     */
    private String content;

    /**
     */
    @NotNull
    @Enumerated
    private Confidentiality confidentiality;

    /**
     */
    @NotNull
    @DBRef
    private UserNomade nomade;

    /**
     */
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date created = new Date();

    /**
     * The parent topic of this subtopic
     */
    @NotNull
    @DBRef
    private Topic parentTopic;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Discussion> discussions = new HashSet<Discussion>();

    /**
     * a set of subtopics related to this subtopic
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @DBRef
    private Set<SubTopic> subTopics = new HashSet<SubTopic>();
}
