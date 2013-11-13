package com.nomade.web;

import java.math.BigInteger;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.nomade.domain.Album;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.UserService;

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

@RequestMapping("/albums")
@Controller
@RooWebScaffold(path = "albums", formBackingObject = Album.class)
public class AlbumController {
	
	@Autowired
	UserService userService;
	@Autowired
	Security securite ;
	
	
	
	@RequestMapping("/myPic")
    public String myPic( Model uiModel, HttpServletRequest httpServletRequest) {
    	
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
	        System.out.print(nomade);
	        return "albums/picManager";
	    }
	 
	 @RequestMapping(value = "delete/{albumId}")
	    public String delete(@PathVariable("albumId") String albumId, Model uiModel) {
	       
	       userService.removeAlbum(albumId, securite.getUserNomade().getUserName());
		 return "albums/picManager";
	    }
	 
	 @ModelAttribute("nomade")
	 public UserNomade nomadePopulate(){
		 
		 return securite!=null?null:securite.getUserNomade();
	 }
	    
	
}
