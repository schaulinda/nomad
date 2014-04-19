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
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooMongoEntity
public class Discussion {

    /**
     */
    @NotNull
    private String title;

    /**
     */
    private String content;

    /**
     * comments related to this discussion
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<Comment>();

    /**
     */
    @DBRef
    private UserNomade nomade;

    /**
     */
    @Enumerated
    private Confidentiality confidentiality;

    /**
     */
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date created = new Date();

    /**
     * The subtopic of this discussion
     */
    @ManyToOne
    @DBRef
    private SubTopic subTopic;

    /**
     */
    @NotNull
    private Boolean frozen;

    /**
     */
    private String photoId;
}
