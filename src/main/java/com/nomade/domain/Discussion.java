package com.nomade.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Enumerated;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

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
    @ManyToOne
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
}
