package com.nomade.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {

	public static void validateImage(MultipartFile image) {
		if (!image.getContentType().equals("image/jpeg")) {
			throw new ImageUploadException("Only JPG images accepted");
		}
	}

	public static void saveImage(String filename, MultipartFile image)
			throws ImageUploadException {
		try {
			/*File file = new File(httpServletRequest.getContextPath() + "/resources/" + filename);
			FileUtils.writeByteArrayToFile(file, image.getBytes());*/
			
			//System.getProperty("user.home")File fileDIr = new File( "./src/main/webapp/images/profil/");
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
