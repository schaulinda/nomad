package com.nomade.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.UserNomade;

@RooService(domainTypes = { com.nomade.domain.UserNomade.class })
public interface UserService {
	
	public List<UserNomade> findByUserName(String userName);
	public List<UserNomade> findByEmail(String email);
	 public void removeAlbum(String albumId, String username);
}
