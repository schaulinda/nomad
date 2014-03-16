package com.nomade.service;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.BeanSubTopicView;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;

@RooService(domainTypes = { com.nomade.domain.SubTopic.class })
public interface SubTopicService {
	public List<SubTopic> findByParentTopic(Topic topic);
	public List<SubTopic> findByParentTopic(Topic topic,int firstResult, int maxResults);
	public int countDiscussion(List<SubTopic> subTopics);
	public int countMessages(List<SubTopic> subTopics);
	public Date getLastMessageDate(List<SubTopic> subTopics);
	public List<BeanSubTopicView>convertSubTopicToBeanSubTopic(List<SubTopic> subTopics);
    public List<SubTopic> findByConfidentiality(Confidentiality confidentiality);
    public List<SubTopic> findByParentTopicAndConfidentiality(Topic topic,Confidentiality confidentiality);
    public List<SubTopic> findByParentTopicAndConfidentiality(Topic topic,Confidentiality confidentiality,int firstResult,int maxResult);

    public List<SubTopic> findByParentTopicAndConfidentialityAndFrozen(Topic topic,Confidentiality confidentiality,Boolean frozen);
    public List<SubTopic> findByParentTopicAndConfidentialityAndFrozen(Topic topic,Confidentiality confidentiality,Boolean frozen,int firstResult, int max);
    public List<SubTopic> findByParentTopicAndFrozen(Topic topic,Boolean frozen);
    public List<SubTopic> findByParentTopicAndFrozen(Topic topic,Boolean frozen,int firstResult, int max);
    
    public SubTopic freezeSubTopic(SubTopic subTopic);
    public SubTopic unFreezeSubTopic(SubTopic subTopic);
}