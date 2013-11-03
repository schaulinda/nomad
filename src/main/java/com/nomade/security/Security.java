package com.nomade.security;

import org.springframework.security.core.userdetails.UserDetails;

import com.nomade.domain.UserNomade;

public interface Security {
	
	public UserDetails getUserDetails();
	
	public  String getUserName();
	
	public UserNomade getUserNomade();

}
