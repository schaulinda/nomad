package com.nomade.service;

import java.util.List;

import com.nomade.domain.UserNomade;


public class UserServiceImpl implements UserService {
	
	 public List<UserNomade> findByUserName(String userName){
	        return userRepository.findByUserName(userName);
	    }
	 
	 public List<UserNomade> findByEmail(String email){
		 return userRepository.findByCompteEmail(email);
	 }
	 
}
