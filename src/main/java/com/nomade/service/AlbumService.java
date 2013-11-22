package com.nomade.service;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.nomade.domain.Album.class })
public interface AlbumService {
	
	
	public String albumName(String idAlbum);
}
