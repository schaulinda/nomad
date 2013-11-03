package com.nomade.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {
	
	private static final Set<String> imagesContentTypes = new HashSet<String>(
			Arrays.asList("image/bmp", "image/gif", "image/jpeg", "image/png",
					"image/tiff", "image/vnd.wap.wbmp", "image/x-icon",
					"image/x-psd", "image/x-xcf"));

	private static final Set<String> imagesFilePrefix = new HashSet<String>(
			Arrays.asList("bmp", "BMP", "png", "PNG", "gif", "GIF", "jpg",
					"JPG", "jpeg", "JPEG", "tiff", "TIFF"));
	
	public static final Boolean isImageByContentType(String contentType) {
		return imagesContentTypes.contains(contentType);
	}

	public static final Boolean isImageByName(String fileName) {
		if (fileName == null)
			return false;
		String extension = FilenameUtils.getExtension(fileName);
		if (extension == null)
			return false;
		return imagesFilePrefix.contains(extension);
	}
	

	public static void validateImage(MultipartFile image) {
		if (!isImageByContentType(image.getContentType())) {
			throw new ImageUploadException("Only images accepted");
		}
		if(!isImageByName(image.getOriginalFilename()))
			throw new ImageUploadException("Only images accepted");
	}

	public static void saveImage(String filename, MultipartFile image)
			throws ImageUploadException {
		try {
			
			File fileDIr = new File(System.getProperty("user.home")+"/");
			/*fileDIr.mkdirs();*/
			File diskFile = new File(fileDIr, filename);
			FileOutputStream fileOutputStream = new FileOutputStream(diskFile);
			IOUtils.copy(image.getInputStream(), fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
		} catch (IOException e) {
			throw new ImageUploadException("Unable to save image", e);
		}
	}

}
