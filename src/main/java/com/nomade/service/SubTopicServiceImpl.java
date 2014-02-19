package com.nomade.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nomade.domain.BeanSubTopicView;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.tools.CollectionUtil;

public class SubTopicServiceImpl implements SubTopicService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	DiscussionService discussionService;
	@Override
	public List<SubTopic> findByParentTopic(Topic topic){
		return subTopicRepository.findByParentTopic(topic);
	}
	@Override
	public List<SubTopic> findByParentTopic(Topic topic,int firstResult,int maxResults){
		return subTopicRepository.findByParentTopic(topic,new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults));
	}

	@Override
	public int countDiscussion(List<SubTopic> subTopics) {
		int discussions = 0 ;
		for (SubTopic subTopic : subTopics) {
			int countDiscussion = discussionService.countDiscussion(subTopic);
			discussions += countDiscussion;
		}
		return discussions;
	}
	@Override
	public int countMessages(List<SubTopic> subTopics) {
		int messages = 0;
		for (SubTopic subTopic : subTopics) {
			int countMessages = discussionService.countMessages(subTopic);
			messages += countMessages;
		}
		return messages;
	}
	public int countMessages(SubTopic subTopic){
		return discussionService.countMessages(subTopic);
	}
	@Override
	public Date getLastMessageDate(List<SubTopic> subTopics) {
		List<Date> lastMessageDates = new ArrayList<Date>();
		for (SubTopic subTopic : subTopics) {
			Date lastMessageDate = discussionService.getLastMessageDate(subTopic);
			lastMessageDates.add(lastMessageDate);
		}
		Collections.sort(lastMessageDates);
		return CollectionUtil.getLastElement(lastMessageDates);
	}

	@Override
	public List<BeanSubTopicView> convertSubTopicToBeanSubTopic(List<SubTopic> subTopics){
		List<BeanSubTopicView> subTopicBeans = new ArrayList<BeanSubTopicView>();
		for (SubTopic subTopic : subTopics) {
			BeanSubTopicView beanSubTopicView = new BeanSubTopicView();
			beanSubTopicView.setSubTopic(subTopic);
			beanSubTopicView.setNumberOfDiscussion(discussionService.countDiscussion(subTopic));
			beanSubTopicView.setNumberOfMessage(discussionService.countMessages(subTopic));
			beanSubTopicView.setLastMessageDate(discussionService.getLastMessageDate(subTopic));
			subTopicBeans.add(beanSubTopicView);
		}
		return subTopicBeans;
	}
}
