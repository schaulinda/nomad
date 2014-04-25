package com.nomade.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.nomade.domain.Album;
import com.nomade.domain.ImageInfo;
import com.nomade.domain.UserNomade;
import com.nomade.service.UserService;

@Service
public class ImageUtil implements ImageUtilInterface {

	@Autowired
	ServletContext servletContext;
	@Autowired
	GridFsTemplate gridFsTemplate;
	@Autowired
	UserService userService;

	@Override
	public boolean isValidate(MultipartFile file) {

		if (file == null)
			return false;

		String fileName = file.getOriginalFilename();

		String mimeType = servletContext.getMimeType(fileName);
		if (mimeType != null) {

			if (mimeType.startsWith("image")) {

				byte[] bytes;
				try {
					bytes = file.getBytes();
					String mimeType2;
					try {
						mimeType2 = Magic.getMagicMatch(bytes, false)
								.getMimeType();
						if (mimeType2.startsWith("image")) {
							return true;
						}
					} catch (MagicParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MagicMatchNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MagicException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
		return false;
	}

	@Override
	public String save(InputStream inputStream, String contentType,
			String filename, ImageInfo imageInfo) {

		DBObject metaData = new BasicDBObject();
		metaData.put("adress", imageInfo.getAdress());
		metaData.put("albumId", imageInfo.getAlbumId());
		metaData.put("datePhoto", imageInfo.getDatePhoto());
		metaData.put("description", imageInfo.getDescription());
		metaData.put("location", imageInfo.getLocation());
		metaData.put("lat", imageInfo.getLat());
		metaData.put("lng", imageInfo.getLng());

		GridFSFile gridFSFile = gridFsTemplate.store(inputStream, filename,
				contentType, metaData);

		return gridFSFile.getId().toString();
	}
	
	@Override
	public String savethumbail(InputStream inputStream, String contentType,String filename) {
		  
		GridFSFile gridFSFile = gridFsTemplate.store(inputStream, filename, contentType);
		
		return gridFSFile.getId().toString();
	}
	

	@Override
	public GridFSDBFile get(String id) {
		return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(
				new ObjectId(id))));
	}

	@Override
	public GridFSDBFile getByFilename(String filename) {
		return gridFsTemplate.findOne(new Query(Criteria.where("filename").is(
				filename)));
	}

	@Override
	public List<GridFSDBFile> getByAlbum(String albumId) {
		return gridFsTemplate.find(new Query(Criteria.where("metadata.albumId")
				.is(albumId)));
	}

	@Override
	public List<String> getPhotoIdByAlbum(String albumId) {
		List<String> idPhoto = new ArrayList<String>();

		Query query = new Query(Criteria.where("metadata.albumId").is(albumId));

		List<GridFSDBFile> list = gridFsTemplate.find(query);

		for (GridFSDBFile file : list) {
			idPhoto.add(file.getId().toString());
		}
		return idPhoto;
	}

	@Override
	public void delePhotoByIdAlbum(String albumId) {

		gridFsTemplate.delete(new Query(Criteria.where("metadata.albumId").is(
				albumId)));
	}

	@Override
	public UserNomade countPhotoByIdAlbum(UserNomade nomade) {
		Iterator<Album> iterator = nomade.getAlbums().iterator();
		List<Album> set = new ArrayList<Album>();
		while(iterator.hasNext()) {
			Album next = iterator.next();
			List<GridFSDBFile> list = gridFsTemplate.find(new Query(Criteria
					.where("metadata.albumId").is(next.get_id())));
			if(list!=null)
				next.setNumPhoto(list.size());
			
			set.add(next);

		}
		nomade.setAlbums(set);
		return nomade;
	}

	@Override
	public List<GridFSDBFile> listFiles() {

		return gridFsTemplate.find(null);
	}
	
	@Override
	public List<String> allImg(UserNomade nomade) {
		
		/*List<String> idPhoto = new ArrayList<String>();
		
		List<GridFSDBFile> list = gridFsTemplate.find(null);

			for (GridFSDBFile file : list) {
			idPhoto.add(file.getId().toString());
		}
		return idPhoto;*/
		List<String> idPhoto = new ArrayList<String>();
	
		try {
			List<Album> albums = nomade.getAlbums();
			for(Album a:albums){
			Query query = new Query(Criteria.where("metadata.albumId").is(""+a.get_id()));

			List<GridFSDBFile> list = gridFsTemplate.find(query);
				
				for (GridFSDBFile file : list) {
					idPhoto.add(file.getId().toString());
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return idPhoto;
	}

	@Override
	public void delete(String id) {
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(
				new ObjectId(id))));
	}

	@Override
	public void removeAlbum(String albumId, UserNomade nomade) {

		nomade.findAndRemAlbum(albumId);
		userService.updateUserNomade(nomade);
		delePhotoByIdAlbum(albumId);

	}

	/*
	 * private static final Set<String> imagesContentTypes = new
	 * HashSet<String>( Arrays.asList("image/bmp", "image/gif", "image/jpeg",
	 * "image/png", "image/tiff", "image/vnd.wap.wbmp", "image/x-icon",
	 * "image/x-psd", "image/x-xcf"));
	 * 
	 * private static final Set<String> imagesFilePrefix = new HashSet<String>(
	 * Arrays.asList("bmp", "BMP", "png", "PNG", "gif", "GIF", "jpg", "JPG",
	 * "jpeg", "JPEG", "tiff", "TIFF"));
	 * 
	 * public static final Boolean isImageByContentType(String contentType) {
	 * return imagesContentTypes.contains(contentType); }
	 * 
	 * public static final Boolean isImageByName(String fileName) { if (fileName
	 * == null) return false; String extension =
	 * FilenameUtils.getExtension(fileName); if (extension == null) return
	 * false; return imagesFilePrefix.contains(extension); }
	 * 
	 * 
	 * public static void validateImage(MultipartFile image) { if
	 * (!isImageByContentType(image.getContentType())) { throw new
	 * ImageUploadException("Only images accepted"); }
	 * if(!isImageByName(image.getOriginalFilename())) throw new
	 * ImageUploadException("Only images accepted"); }
	 * 
	 * public static void saveImage(String filename, MultipartFile image) throws
	 * ImageUploadException { try {
	 * 
	 * File fileDIr = new File(System.getProperty("user.home")+"/");
	 * fileDIr.mkdirs(); File diskFile = new File(fileDIr, filename);
	 * FileOutputStream fileOutputStream = new FileOutputStream(diskFile);
	 * IOUtils.copy(image.getInputStream(), fileOutputStream);
	 * IOUtils.closeQuietly(fileOutputStream); } catch (IOException e) { throw
	 * new ImageUploadException("Unable to save image", e); } }
	 */
}
