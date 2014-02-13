package com.nomade.domain;
import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author bwa
 * <p>Used to display topics in the mainPage.</p>
 */
@RooJavaBean
@RooToString
@RooEquals
@RooSerializable
public class BeanTopicManager {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    @ManyToOne
    private Topic topic;

    /**
     */
    private int nbOfDiscussion;

    /**
     */
    private int nbOfMessages;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date lastMessageDate;
}
