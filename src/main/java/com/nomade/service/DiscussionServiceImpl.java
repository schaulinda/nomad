package com.nomade.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;

import com.nomade.domain.Comment;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.domain.UserNomade;
import com.nomade.tools.CollectionUtil;

public class DiscussionServiceImpl implements DiscussionService {

	@Override
	public int countDiscussion(SubTopic subTopic){
		return discussionRepository.findBySubTopic(subTopic).size();
	}
	@Override
	public int countMessages(SubTopic subTopic){
		Collection<Comment> subTopicComments = findSubTopicComments(subTopic);
		return subTopicComments.size();
	}
	@Override
	public Date getLastMessageDate(SubTopic subTopics){
		List<Comment> subTopicComments = findSubTopicComments(subTopics);
		Collections.sort(subTopicComments, new Comparator<Comment>() {
			@Override
			public int compare(Comment comment1, Comment comment2) {
				return comment1.getCreated().compareTo(comment2.getCreated());
			}
		});
		if(subTopicComments.size() <= 0) {
			return null;
		}
		return CollectionUtil.getLastElement(subTopicComments).getCreated();
	}
	@Override
	public List<Comment> findSubTopicComments(SubTopic subTopic){
		List<Discussion> discussions = discussionRepository.findBySubTopic(subTopic);
		Set<Comment> subTopicComments = new HashSet<Comment>();
		for (Discussion discussion : discussions) {
			Set<Comment> discussionComments = discussion.getComments();
			subTopicComments.addAll(discussionComments);
		}
		return new ArrayList<Comment>(subTopicComments);
	}

	@Override
    public Discussion findByTitle(String title){
		return discussionRepository.findByTitle(title);
    }
	@Override
    public List<Discussion> findByNomade(UserNomade nomade){
    	return discussionRepository.findByNomade(nomade);
    }
	@Override
    public List<Discussion> findByConfidentiality(Confidentiality confidentiality){
    	return discussionRepository.findByConfidentiality(confidentiality);
    }
	@Override
    public List<Discussion> findByCreated(Date date){
    	return discussionRepository.findByCreated(date);
    }

	@Override
    public List<Discussion> findBySubTopic(SubTopic subTopic){
		return discussionRepository.findBySubTopic(subTopic);
    }
	@Override
    public List<Discussion> findBySubTopic(SubTopic subTopic,int firstResult,int maxResults){
		checkAndFixPageParams(firstResult, maxResults);
		return discussionRepository.findBySubTopic(subTopic, new PageRequest(firstResult, maxResults));
    }
	protected void checkAndFixPageParams(int firstResult, int maxResults){
		if (firstResult < 0) {
			firstResult = 0;
		} else if (maxResults < 0) {
			maxResults= 10;
		}
	}
	@Override
    public List<Discussion> findBySubTopicAndConfidentiality(SubTopic subTopic,Confidentiality confidentiality){
    	return discussionRepository.findBySubTopicAndConfidentiality(subTopic, confidentiality);
    }
	@Override
    public List<Discussion> findBySubTopicAndConfidentiality(SubTopic topic,Confidentiality confidentiality,int firstResult,int maxResults){
		checkAndFixPageParams(firstResult, maxResults);
    	return discussionRepository.findBySubTopicAndConfidentiality(topic, confidentiality, new PageRequest(firstResult, maxResults));
    }
}
