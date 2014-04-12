// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.service;

import com.nomade.domain.SubTopic;
import com.nomade.repository.SubTopicRepository;
import com.nomade.service.SubTopicServiceImpl;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SubTopicServiceImpl_Roo_Service {
    
    declare @type: SubTopicServiceImpl: @Service;
    
    declare @type: SubTopicServiceImpl: @Transactional;
    
    @Autowired
    SubTopicRepository SubTopicServiceImpl.subTopicRepository;
    
    public long SubTopicServiceImpl.countAllSubTopics() {
        return subTopicRepository.count();
    }
    
    public void SubTopicServiceImpl.deleteSubTopic(SubTopic subTopic) {
        subTopicRepository.delete(subTopic);
    }
    
    public SubTopic SubTopicServiceImpl.findSubTopic(BigInteger id) {
        return subTopicRepository.findOne(id);
    }
    
    public List<SubTopic> SubTopicServiceImpl.findAllSubTopics() {
        return subTopicRepository.findAll();
    }
    
    public List<SubTopic> SubTopicServiceImpl.findSubTopicEntries(int firstResult, int maxResults) {
        return subTopicRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void SubTopicServiceImpl.saveSubTopic(SubTopic subTopic) {
        subTopicRepository.save(subTopic);
    }
    
    public SubTopic SubTopicServiceImpl.updateSubTopic(SubTopic subTopic) {
        return subTopicRepository.save(subTopic);
    }
    
}