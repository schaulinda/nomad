package com.nomade.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nomade.domain.Comment;
import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;

public class SubTopicServiceImpl implements SubTopicService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public List<SubTopic> findByParentTopic(Topic topic){
		return subTopicRepository.findByParentTopic(topic);
	}

	@Override
	public int countDiscussion(List<SubTopic> subTopics) {
		List<Discussion> discussions = new ArrayList<Discussion>();
		for (SubTopic subTopic : subTopics) {
			Collection<Discussion> subTopicDiscussion = subTopic.getDiscussions();
			discussions.addAll(subTopicDiscussion);
		}
		return discussions.size();
	}
	@Override
	public int countMessages(List<SubTopic> subTopics) {
		List<Discussion> discussions = new ArrayList<Discussion>();
		for (SubTopic subTopic : subTopics) {
			Collection<Discussion> subTopicDiscussion = subTopic.getDiscussions();
			discussions.addAll(subTopicDiscussion);
		}
		List<Comment> comments = new ArrayList<Comment>();
		for (Discussion discussion : discussions) {
			Collection<Comment> discussionComments = discussion.getComments();
			comments.addAll(discussionComments);
		}
		return comments.size();
	}
	public int countMessages(SubTopic subTopic){
		return 0;
	}
	@Override
	public Date getLastMessageDate(List<SubTopic> subTopics) {
		List<Discussion> discussions = new ArrayList<Discussion>();
		for (SubTopic subTopic : subTopics) {
			Collection<Discussion> subTopicDiscussion = subTopic.getDiscussions();
			discussions.addAll(subTopicDiscussion);
		}
		List<Comment> comments = new ArrayList<Comment>();
		for (Discussion discussion : discussions) {
			Collection<Comment> discussionComments = discussion.getComments();
			comments.addAll(discussionComments);
		}
		Collections.sort(comments, new Comparator<Comment>() {
			@Override
			public int compare(Comment o1, Comment o2) {
				int result = 0;
				if(o1.getCreated().before(o2.getCreated())){
					result = -1;
				}else if(o1.getCreated().after(o2.getCreated())){
					result = 1;
				}
				return result;
			}
		});
		int lastIndex = 0 ;
		if(comments.size() > 0){
			lastIndex = comments.size()-1;
		}
		if(lastIndex == 0) {
			return null ;
		}
		return comments.get(lastIndex).getCreated();//get the date of the last element.
	}
}
