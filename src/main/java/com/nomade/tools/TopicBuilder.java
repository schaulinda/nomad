package com.nomade.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nomade.domain.Confidentiality;
import com.nomade.domain.Topic;
import com.nomade.domain.UserNomade;

public class TopicBuilder {
	private List<Topic> topics = new ArrayList<Topic>();
	
	private TopicBuilder() {
	}
	public static TopicBuilder get(){
		return new TopicBuilder();
	}
	
	public TopicBuilder addTopic(String title,String content, Confidentiality confidentiality,UserNomade nomade){
		Topic topic = new Topic();
		topic.setConfidentiality(confidentiality);
		topic.setContent(content);
		topic.setCreated(new Date());
		topic.setNomade(nomade);
		topic.setTitle(title);
		topics.add(topic);
		return this;
	}
	
	public List<Topic> getTopics() {
		return topics;
	}
	
}
