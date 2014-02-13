package com.nomade.domain;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * @author bwa
 *	<p>A convenient bean used to display subtopics, in a subtopic list. </p>
 *<p> This bean carry in addition the number of discussion per topcis,
 *the number of messages and the date of the lastMessage </p>
 */
@RooJavaBean
@RooToString
@RooEquals
@RooSerializable
public class BeanSubTopicView {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     */
    private SubTopic subTopic;

    /**
     */
    private int numberOfDiscussion;

    /**
     */
    private int numberOfMessage;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date lastMessageDate;
}
