package com.nomade.tools;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSDBFile;
import com.nomade.domain.ImageInfo;

public interface ImageUtilInterface {
	
	public boolean isValidate(MultipartFile file);
	
	public String save(InputStream inputStream, String contentType, String filename, ImageInfo imageInfo);

	public GridFSDBFile getByFilename(String filename);
	
	public List<GridFSDBFile> getByAlbum(String albumId);

	public GridFSDBFile get(String id);
	
	public List<GridFSDBFile> listFiles();

	List<String> getPhotoIdByAlbum(String albumId);

}
