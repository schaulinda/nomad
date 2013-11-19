package com.nomade.domain;

import java.util.List;

public class BeanPictureManager {
	
	private String tabManager;
	
	private boolean formInfoImg;
	
	private String idAlbum;
	
	private String tabPhoto;
	
	private  List<String> listIdPhoto;
	
	private String photoSave;
	
	

	public BeanPictureManager(String tabManager, boolean formInfoImg,
			String idAlbum, String tabPhoto, List<String> listIdPhoto,
			String photoSave) {
		super();
		this.tabManager = tabManager;
		this.formInfoImg = formInfoImg;
		this.idAlbum = idAlbum;
		this.tabPhoto = tabPhoto;
		this.listIdPhoto = listIdPhoto;
		this.photoSave = photoSave;
	}

	public String getTabManager() {
		return tabManager;
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
