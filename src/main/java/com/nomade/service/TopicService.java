package com.nomade.service;
import java.util.Date;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Topic;

@RooService(domainTypes = { com.nomade.domain.Topic.class })
public interface TopicService {
	public int countDiscussion(Topic topic);
	public int countMessages(Topic topic);
	public Date getLastMessageDate(Topic topic);
}
