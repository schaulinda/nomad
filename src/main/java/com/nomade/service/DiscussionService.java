package com.nomade.service;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Comment;
import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.tools.ForumQuery;

@RooService(domainTypes = { com.nomade.domain.Discussion.class })
public interface DiscussionService extends ForumQuery<Discussion>{
	public int countDiscussion(SubTopic subTopics);
	public int countMessages(SubTopic subTopics);
	public Date getLastMessageDate(SubTopic subTopics);
	public List<Comment> findSubTopicComments(SubTopic subTopic);
    public List<Discussion> findBySubTopic(SubTopic subTopic);
    public List<Discussion> findBySubTopic(SubTopic subTopic,int firstResult,int maxResults);
}
