package com.nomade.domain;
import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooEquals
@RooSerializable
public class BeanForumManager {

    /**
     * a collection of topics
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Topic> topics = new HashSet<Topic>();

    /**
     * a collection of subtopics
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<SubTopic> subTopics = new HashSet<SubTopic>();
}
