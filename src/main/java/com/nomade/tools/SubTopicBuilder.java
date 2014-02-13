package com.nomade.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nomade.domain.Confidentiality;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.domain.UserNomade;

public class SubTopicBuilder {
	private List<SubTopic> subTopics = new ArrayList<SubTopic>();
	
	private SubTopicBuilder (){
		
	}
	/**
	 * Create an instance of a subtopic builder.
	 * @return {@link SubTopicBuilder SubTopicBuilder}
	 */
	public static SubTopicBuilder get(){
		return new SubTopicBuilder();
	}
	
	/**
	 * <p>Create a subtopic and add it to the list subtopics.</p>
	 * @param title
	 * @param content
	 * @param confidentiality
	 * @param nomade
	 * @param parentTopic
	 * @param discussions
	 * @return {@link SubTopicBuilder SubTopicBuilder}
	 */
	public SubTopicBuilder addSubTopic(String title,String content, Confidentiality confidentiality,UserNomade nomade, Topic parentTopic){
		SubTopic subTopic = new SubTopic();
		subTopic.setConfidentiality(confidentiality);
		subTopic.setContent(content);
		subTopic.setCreated(new Date());
		subTopic.setNomade(nomade);
		subTopic.setParentTopic(parentTopic);
		subTopic.setTitle(title);
		getSubTopics().add(subTopic);
		return this;
	}
	/**
	 * @return the subTopics
	 */
	public List<SubTopic> getSubTopics() {
		return subTopics;
	}
}
