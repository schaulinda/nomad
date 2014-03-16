package com.nomade.service;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.Comment;
import com.nomade.domain.Confidentiality;
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
    public List<Discussion> findByConfidentiality(Confidentiality confidentiality);
    public List<Discussion> findBySubTopicAndConfidentiality(SubTopic topic,Confidentiality confidentiality);
    public List<Discussion> findBySubTopicAndConfidentiality(SubTopic topic,Confidentiality confidentiality,int firstResult,int maxResults);
    public List<Discussion> findBySubTopicAndConfidentialityAndFrozen(SubTopic subTopic,Confidentiality confidentiality,Boolean frozen);
    public List<Discussion> findBySubTopicAndConfidentialityAndFrozen(SubTopic subTopic,Confidentiality confidentiality,Boolean frozen,int firstResult,int maxResults);
    public List<Discussion> findBySubTopicAndFrozen(SubTopic subTopic,Boolean frozen);
    public List<Discussion> findBySubTopicAndFrozen(SubTopic subTopic,Boolean frozen,int firstResult,int maxResults);
    
    public Discussion freezeDiscussion(Discussion discussion);
    public Discussion unFreezeDiscussion(Discussion discussion);
}