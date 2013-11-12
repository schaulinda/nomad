package com.nomade.web;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.Album;
import com.nomade.security.Security;
import com.nomade.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
}
