package com.nomade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.nomade.domain.UserNomade;


public class UserServiceImpl implements UserService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	 public List<UserNomade> findByUserName(String userName){
	        return userRepository.findByUserName(userName);
	    }
	 
	 public List<UserNomade> findByEmail(String email){
		 return userRepository.findByCompteEmail(email);
	 }
	 
	
	 
}
