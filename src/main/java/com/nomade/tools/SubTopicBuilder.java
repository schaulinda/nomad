package com.nomade.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

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
	public SubTopicBuilder addSubTopic(String title,String content, Confidentiality confidentiality,UserNomade nomade, Topic parentTopic,Boolean frozen){
		SubTopic subTopic = new SubTopic();
		subTopic.setConfidentiality(confidentiality);
		subTopic.setContent(content);
		subTopic.setCreated(new Date());
		subTopic.setNomade(nomade);
		subTopic.setParentTopic(parentTopic);
		subTopic.setTitle(title);
		subTopic.setFrozen(frozen);
		subTopic.setIsADefaultSubTopic(true);
		getSubTopics().add(subTopic);
		return this;
	}

	/**
	 * add a public, not frozen subtopic.
	 * @param title
	 * @param nomade
	 * @param topic
	 * @param i18nKey
	 * @return
	 */
	public SubTopicBuilder addPublicNonFrozenSubTopic(String title,UserNomade nomade, Topic topic,String i18nKey){
		SubTopic subTopic = new SubTopic();
		subTopic.setConfidentiality(Confidentiality.Publique);
		subTopic.setContent("");
		subTopic.setCreated(new Date());
		subTopic.setNomade(nomade);
		subTopic.setParentTopic(topic);
		subTopic.setTitle(title);
		subTopic.setFrozen(false);
		subTopic.setInternationalizationPropertyKey(i18nKey);
		subTopic.setIsADefaultSubTopic(true);
		getSubTopics().add(subTopic);
		return this;
	}

	/**
	 * Add a public, not frozen subtopic.
	 * @param title
	 * @param nomade
	 * @param topic
	 * @param parentSubTopic
	 * @param i18nKey
	 * @return
	 */
	public SubTopicBuilder addSubPublicNonFrozenSubTopic(String title,UserNomade nomade, Topic topic,SubTopic parentSubTopic,String i18nKey){
		SubTopic subTopic = new SubTopic();
		subTopic.setConfidentiality(Confidentiality.Publique);
		subTopic.setContent("");
		subTopic.setCreated(new Date());
		subTopic.setNomade(nomade);
		subTopic.setParentTopic(topic);
		subTopic.setTitle(title);
		subTopic.setFrozen(false);
		subTopic.setParentSubTopic(parentSubTopic);
		subTopic.setInternationalizationPropertyKey(i18nKey);
		subTopic.setIsADefaultSubTopic(true);
		getSubTopics().add(subTopic);
		return this;
	}
	public SubTopicBuilder addSubTopic(String title,String content, Confidentiality confidentiality,UserNomade nomade, Topic parentTopic,Boolean frozen,String i18nKey){
		SubTopic subTopic = new SubTopic();
		subTopic.setConfidentiality(confidentiality);
		subTopic.setContent(content);
		subTopic.setCreated(new Date());
		subTopic.setNomade(nomade);
		subTopic.setParentTopic(parentTopic);
		subTopic.setTitle(title);
		subTopic.setFrozen(frozen);
		subTopic.setInternationalizationPropertyKey(i18nKey);
		getSubTopics().add(subTopic);
		return this;
	}
	public SubTopic getFirstSubTopic() {
		Assert.notEmpty(subTopics, "The subtopics list should not be empty here !");
		return subTopics.get(0);
	}
	/**
	 * @return the subTopics
	 */
	public List<SubTopic> getSubTopics() {
		return subTopics;
	}
}
