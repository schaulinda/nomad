package com.nomade.domain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class BeanPictureManager {
	
	private String tabManager;
	
	private boolean formInfoImg;
	
	private String idAlbum;
	
	private String tabPhoto;
	
	private  List<String> listIdPhoto;
	
	private String photoSave;
	
	private String albumName;
	
	private boolean isBackLink;
	
	private HttpServletRequest httpServletRequest;
	

	public boolean isBackLink() {
		
		Object attribute = httpServletRequest.getSession(true).getAttribute("backLink");
		if(attribute==null)
			return false;
		else
			return true;
	}

	public void setBackLink(boolean isBackLink) {
		this.isBackLink = isBackLink;
	}

	public BeanPictureManager(String tabManager, boolean formInfoImg,
			String idAlbum, String tabPhoto, List<String> listIdPhoto,
			String photoSave,HttpServletRequest httpServletRequest) {
		super();
		this.tabManager = tabManager;
		this.formInfoImg = formInfoImg;
		this.idAlbum = idAlbum;
		this.tabPhoto = tabPhoto;
		this.listIdPhoto = listIdPhoto;
		this.photoSave = photoSave;
		this.httpServletRequest = httpServletRequest;
	}

	public String getTabManager() {
		return tabManager;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public void setTabManager(String tabManager) {
		this.tabManager = tabManager;
	}

	public boolean isFormInfoImg() {
		return formInfoImg;
	}

	public void setFormInfoImg(boolean formInfoImg) {
		this.formInfoImg = formInfoImg;
	}

	public String getIdAlbum() {
		return idAlbum;
	}

	public void setIdAlbum(String idAlbum) {
		this.idAlbum = idAlbum;
	}

	public String getTabPhoto() {
		return tabPhoto;
	}

	public void setTabPhoto(String tabPhoto) {
		this.tabPhoto = tabPhoto;
	}

	public  List<String> getListIdPhoto() {
		return listIdPhoto;
	}

	public void setListIdPhoto( List<String> listIdPhoto) {
		this.listIdPhoto = listIdPhoto;
	}

	public String getPhotoSave() {
		return photoSave;
	}

	public void setPhotoSave(String photoSave) {
		this.photoSave = photoSave;
	}

}
