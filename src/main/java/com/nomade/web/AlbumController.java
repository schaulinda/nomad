package com.nomade.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.nomade.domain.Album;
import com.nomade.domain.BeanPictureManager;
import com.nomade.domain.ImageInfo;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.UserService;
import com.nomade.tools.ImageUtil;

@RequestMapping("/albums")
@Controller
@RooWebScaffold(path = "albums", formBackingObject = Album.class)
public class AlbumController {

	@Autowired
	UserService userService;
	@Autowired
	Security securite;
	@Autowired
	ImageUtil imageUtil;

	private BeanPictureManager dataPic(UserNomade nomade,HttpServletRequest httpServletRequest) {

		String idAlbum;
		List<String> listIdPhoto;
		List<Album> albums = nomade.getAlbums();
		if (albums != null && albums.size() > 0) {

			Album album = albums.iterator().next();
			idAlbum = album.get_id().toString();
			listIdPhoto = imageUtil.getPhotoIdByAlbum(idAlbum);

		} else {
			idAlbum = null;
			listIdPhoto = null;
		}

		return new BeanPictureManager("active", false, idAlbum, "",
				listIdPhoto, "",httpServletRequest);

	}

	// arrive to piture manager
	@RequestMapping("/myPic")
	public String myPic(
			@RequestParam(value = "backLink", required = false) String backLink,
			Model uiModel, HttpServletRequest httpServletRequest) {

		httpServletRequest.getSession(true).setAttribute("backLink", backLink);
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("beanPictureManager", dataPic(nomade,httpServletRequest));
		uiModel.addAttribute("nomade", nomade);
		return "albums/picManager";
	}

	// create album
	@RequestMapping("create")
	public String create(@RequestParam("albumName") String albumName,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (albumName == "" || albumName == null) {
			uiModel.addAttribute("albumName", albumName);

			UserNomade nomade = securite.getUserNomade();
			uiModel.addAttribute("nomade", nomade);
			uiModel.addAttribute("beanPictureManager", dataPic(nomade,httpServletRequest));
			uiModel.addAttribute("errorAlbum", "name can not be null");
			return "albums/picManager";
		}
		uiModel.asMap().clear();
		UserNomade nomade = securite.getUserNomade();
		Album album = new Album(albumName, new Date());
		nomade.getAlbums().add(album);
		// nomade.orderAlbumByDate();
		userService.updateUserNomade(nomade);

		BeanPictureManager beanPictureManager = new BeanPictureManager(
				"active", false, album.get_id().toString(), "", null, "",httpServletRequest);
		uiModel.addAttribute("beanPictureManager", beanPictureManager);
		uiModel.addAttribute("nomade", securite.getUserNomade());
		return "albums/picManager";
	}

	// delete album
	@RequestMapping(value = "delete/{albumId}")
	public String delete(@PathVariable("albumId") String albumId, Model uiModel,HttpServletRequest httpServletRequest) {
		imageUtil.delePhotoByIdAlbum(albumId);
		imageUtil.removeAlbum(albumId, securite.getUserNomade());
		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("beanPictureManager", dataPic(nomade,httpServletRequest));
		return "albums/picManager";
	}

	// images of an album
	@RequestMapping(value = "image/{albumId}")
	public String photoAlbum(@PathVariable("albumId") String albumId,
			Model uiModel,HttpServletRequest httpServletRequest) {

		List<String> photoIdByAlbum = imageUtil.getPhotoIdByAlbum(albumId);
		uiModel.addAttribute("nomade", securite.getUserNomade());
		BeanPictureManager beanPictureManager = new BeanPictureManager(
				"active", false, albumId, "", photoIdByAlbum, "",httpServletRequest);
		uiModel.addAttribute("beanPictureManager", beanPictureManager);
		return "albums/picManager";
	}

	// upload an image file
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(Model uiModel, HttpServletRequest httpServletRequest,
			@RequestParam("fileImage") MultipartFile fileImage,
			@RequestParam("idAlbum") String idAlbum) {

		UserNomade nomade = securite.getUserNomade();
		uiModel.addAttribute("nomade", nomade);

		if (idAlbum == null || StringUtils.isEmpty(idAlbum)
				|| StringUtils.isBlank(idAlbum)) {
			BeanPictureManager beanPictureManager = new BeanPictureManager("",
					false, idAlbum, "active", null, "",httpServletRequest);
			uiModel.addAttribute("beanPictureManager", beanPictureManager);
			uiModel.addAttribute("error", "select or create a new album!");
			return "albums/picManager";

		}
		// id non null
		List<String> photoIdByAlbum = imageUtil.getPhotoIdByAlbum(idAlbum);

		if (fileImage.getSize() > 900000) {

			BeanPictureManager beanPictureManager = new BeanPictureManager("",
					false, idAlbum, "active", photoIdByAlbum, "",httpServletRequest);
			uiModel.addAttribute("beanPictureManager", beanPictureManager);
			uiModel.addAttribute("error", "max size permited is 2M!");
			return "albums/picManager";

		}

		if (!imageUtil.isValidate(fileImage)) {

			uiModel.addAttribute("error", "votre fichier n'est pas valide!");
			BeanPictureManager beanPictureManager = new BeanPictureManager("",
					false, idAlbum, "active", photoIdByAlbum, "",httpServletRequest);
			uiModel.addAttribute("beanPictureManager", beanPictureManager);
		} else {

			try {
				ImageInfo imageInfo = new ImageInfo();
				imageInfo.setAlbumId(idAlbum);

				String idPhotoSsave = imageUtil.save(
						fileImage.getInputStream(), fileImage.getContentType(),
						fileImage.getOriginalFilename(), imageInfo);
				nomade.findAndModifAlbum(idAlbum, 1);
				userService.updateUserNomade(nomade);
				uiModel.addAttribute("nomade", securite.getUserNomade());
				BeanPictureManager beanPictureManager = new BeanPictureManager(
						"", true, idAlbum, "active", photoIdByAlbum,
						idPhotoSsave,httpServletRequest);
				uiModel.addAttribute("beanPictureManager", beanPictureManager);
			} catch (IOException e) {
				uiModel.addAttribute("error", "votre fichier n'est pas valide!");
				BeanPictureManager beanPictureManager = new BeanPictureManager(
						"", false, idAlbum, "active", photoIdByAlbum, "",httpServletRequest);
				uiModel.addAttribute("beanPictureManager", beanPictureManager);
				e.printStackTrace();
			}

		}

		return "albums/picManager";
	}

	@RequestMapping(value = "delete/{albumId}/{photoId}")
	public String deleteSavePhoto(@PathVariable("photoId") String photoId,
			@PathVariable("albumId") String albumId, Model uiModel,HttpServletRequest httpServletRequest) {

		imageUtil.delete(photoId);
		UserNomade nomade = securite.getUserNomade();
		nomade.findAndModifAlbum(albumId, -1);
		userService.updateUserNomade(nomade);
		uiModel.addAttribute("nomade", securite.getUserNomade());
		BeanPictureManager beanPictureManager = new BeanPictureManager("",
				false, albumId, "active", imageUtil.getPhotoIdByAlbum(albumId),
				"",httpServletRequest);
		uiModel.addAttribute("beanPictureManager", beanPictureManager);

		return "albums/picManager";
	}

	@RequestMapping(value = "cancel/{albumId}/{photoId}")
	public String cancelSavePhoto(@PathVariable("photoId") String photoId,
			@PathVariable("albumId") String albumId, Model uiModel,HttpServletRequest httpServletRequest) {

		uiModel.addAttribute("nomade", securite.getUserNomade());
		BeanPictureManager beanPictureManager = new BeanPictureManager("",
				false, albumId, "active", imageUtil.getPhotoIdByAlbum(albumId),
				"",httpServletRequest);
		uiModel.addAttribute("beanPictureManager", beanPictureManager);

		return "albums/picManager";
	}

	@RequestMapping(value = "/saveImageInfo", method = RequestMethod.POST)
	public String saveImageInfo(
			Model uiModel,
			HttpServletRequest httpServletRequest,
			@RequestParam("idPhoto") String idPhoto,
			@RequestParam("addresspicker") String addresspicker,
			@RequestParam("lat") String lat,
			@RequestParam("lng") String lng,
			@RequestParam("description") String description,
			@RequestParam("datePhoto") @DateTimeFormat(pattern = "dd-MM-yyyy") Date datePhoto) {

		double double1 = 0;
		double double2 = 0;
		try {
			double1 = Double.parseDouble(lng);
			double2 = Double.parseDouble(lat);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			double1 = 0;
			double2 = 0;
		}
		GridFSDBFile gridFSDBFile = imageUtil.get(idPhoto);

		DBObject metaData = gridFSDBFile.getMetaData();
		String stringId = metaData.get("albumId").toString();

		ImageInfo imageInfo = new ImageInfo(addresspicker, datePhoto,
				description, stringId, double1, double2);

		imageUtil.save(gridFSDBFile.getInputStream(),
				gridFSDBFile.getContentType(), gridFSDBFile.getFilename(),
				imageInfo);
		imageUtil.delete(idPhoto);

		uiModel.addAttribute("nomade", securite.getUserNomade());
		BeanPictureManager beanPictureManager = new BeanPictureManager("",
				false, stringId, "active",
				imageUtil.getPhotoIdByAlbum(stringId), "",httpServletRequest);
		uiModel.addAttribute("beanPictureManager", beanPictureManager);
		return "albums/picManager";

	}

	@RequestMapping(value = "viewImg/{imgId}")
	public String viewImg(@PathVariable("imgId") String imgId, Model uiModel,HttpServletRequest httpServletRequest) {

		DBObject metaData = imageUtil.get(imgId).getMetaData();

		ImageInfo imageInfo = new ImageInfo(metaData);

		uiModel.addAttribute("nomade", securite.getUserNomade());
		BeanPictureManager beanPictureManager = new BeanPictureManager("",
				true, imageInfo.getAlbumId(), "active",
				imageUtil.getPhotoIdByAlbum(imageInfo.getAlbumId()), imgId,httpServletRequest);
		uiModel.addAttribute("beanPictureManager", beanPictureManager);

		uiModel.addAttribute("datePhoto", imageInfo.getDatePhoto());
		uiModel.addAttribute("description", imageInfo.getDescription());
		uiModel.addAttribute("lat", imageInfo.getLat());
		uiModel.addAttribute("lng", imageInfo.getLng());
		uiModel.addAttribute("addresspicker", imageInfo.getAdress());

		return "albums/picManager";
	}

	@RequestMapping(value = "imageRenderNet/{id}")
	@ResponseBody
	public byte[] getImageGrdFormat(@PathVariable("id") String id, Model uiModel) {
		byte[] byteArray;
		InputStream inputStream = imageUtil.get(id).getInputStream();

		try {
			byteArray = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return byteArray;

	}
	
	// return an image
	@RequestMapping(value = "imageRender/{id}")
	@ResponseBody
	public byte[] getImage(@PathVariable("id") String id, Model uiModel) {
		byte[] byteArray;
		GridFSDBFile gridFSDBFile = imageUtil.get(id);
		InputStream inputStream = gridFSDBFile.getInputStream();
		String contentType = gridFSDBFile.getContentType();
		contentType = StringUtils.split(contentType, "/")[1];
		
		try {//render thumbnail
			BufferedImage imBuff = ImageIO.read(inputStream);
			BufferedImage scaledImg = Scalr.resize(imBuff, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
		               150, 100, Scalr.OP_ANTIALIAS);
			ByteArrayOutputStream bas = new ByteArrayOutputStream();
			ImageIO.write(scaledImg, contentType, bas);
			byteArray = bas.toByteArray();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			byteArray=null;
		}

		return byteArray;

	}

}
