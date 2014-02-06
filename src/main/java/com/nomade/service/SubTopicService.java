package com.nomade.service;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;

@RooService(domainTypes = { com.nomade.domain.SubTopic.class })
public interface SubTopicService {
	public List<SubTopic> findByParentTopic(Topic topic);
	public int countDiscussion(List<SubTopic> subTopics);
	public int countMessages(List<SubTopic> subTopics);
	public Date getLastMessageDate(List<SubTopic> subTopics);
}
