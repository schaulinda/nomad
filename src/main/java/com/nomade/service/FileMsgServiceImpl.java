package com.nomade.service;

import java.util.List;

import com.nomade.domain.FileMsg;
import com.nomade.domain.UserNomade;

public class FileMsgServiceImpl implements FileMsgService {
	
	public List<FileMsg> findByMeAndOther(UserNomade nomad1, UserNomade nomad2){
		
		fileMsgRepository.findByNomad1AndNomad2OrNomad2AndNomad1(nomad1, nomad2, nomad2, nomad1);
		
		return fileMsgRepository.findByNomad1AndNomad2OrNomad2AndNomad1(nomad1, nomad2, nomad2, nomad1);
		
	}
	
	public List<FileMsg> findMyFileMsg(UserNomade nomad){
		
		return fileMsgRepository.findByNomad1OrNomad2(nomad, nomad);
	}
}
