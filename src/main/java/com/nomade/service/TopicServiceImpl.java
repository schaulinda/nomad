package com.nomade.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;

public class TopicServiceImpl implements TopicService {
	@Autowired
	SubTopicService subTopicService;
	
	@Override
	public int countDiscussion(Topic topic) {
		List<SubTopic> subTopics = subTopicService.findByParentTopic(topic);
		return subTopicService.countDiscussion(subTopics);
	}
	
	@Override
	public int countMessages(Topic topic) {
		List<SubTopic> subTopics = subTopicService.findByParentTopic(topic);
		return subTopicService.countMessages(subTopics);
	}
	@Override
	public Date getLastMessageDate(Topic topic) {
		List<SubTopic> subTopics = subTopicService.findByParentTopic(topic);
		return subTopicService.getLastMessageDate(subTopics);
	}
}
