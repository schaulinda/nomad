package com.nomade.web;

import java.math.BigInteger;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.nomade.domain.Album;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.UserService;
import com.nomade.tools.ImageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/albums")
@Controller
@RooWebScaffold(path = "albums", formBackingObject = Album.class)
public class AlbumController {
	
	@Autowired
	UserService userService;
	@Autowired
	Security securite ;
	@Autowired
	ImageUtil imageUtil;
	
	
	
	@RequestMapping("/myPic")
    public String myPic( Model uiModel, HttpServletRequest httpServletRequest) {
		 uiModel.addAttribute("nomade", securite.getUserNomade());
    	return "albums/picManager";
    }
	
	 @RequestMapping("create")
	    public String create(@RequestParam("albumName") String albumName, Model uiModel, HttpServletRequest httpServletRequest) {
	        if (albumName == "" || albumName == null) {
	            uiModel.addAttribute("albumName", albumName);
	            uiModel.addAttribute("errorAlbum", "name already exist");
	            return "albums/picManager";
	        }
	        uiModel.asMap().clear(); 
	        UserNomade nomade = securite.getUserNomade();
	        nomade.getAlbums().add(new Album(albumName, new Date()));
	        userService.updateUserNomade(nomade);
	        uiModel.addAttribute("nomade", nomade);
	      
	        return "albums/picManager";
	    }
	 
	 @RequestMapping(value = "delete/{albumId}")
	    public String delete(@PathVariable("albumId") String albumId, Model uiModel) {
	       
	       userService.removeAlbum(albumId, securite.getUserNomade().getUserName());
		 return "albums/picManager";
	    }
	 
	 @RequestMapping(value = "/upload", method = RequestMethod.POST)
		public String upload(Model uiModel,
				HttpServletRequest httpServletRequest,
				@RequestParam(value = "photo", required = true) MultipartFile photo,
				@RequestParam("idAlbum") String idAlbum) {
		 
		 UserNomade nomade = securite.getUserNomade();
		 if(imageUtil.isValidate(photo)){
			 
			 uiModel.addAttribute("error", "votre fichier n'est pas valide!");
		 }else{
			 
			 
			 
		 }
		 
		
		 uiModel.addAttribute("nomade", nomade);
		 return "albums/picManager";
	 }
	 
	    
	
}
