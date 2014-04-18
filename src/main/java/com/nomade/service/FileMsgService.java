package com.nomade.service;

import org.springframework.roo.addon.layers.service.RooService;

import com.nomade.domain.FileMsg;
import com.nomade.domain.UserNomade;
import java.util.List;

@RooService(domainTypes = { com.nomade.domain.FileMsg.class })
public interface FileMsgService {
	
	public List<FileMsg> findByMeAndOther(UserNomade nomad1, UserNomade nomad2);
	public List<FileMsg> findMyFileMsg(UserNomade nomad);
}
