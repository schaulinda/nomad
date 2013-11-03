package com.nomade.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nomade.domain.BeanRegister;
import com.nomade.domain.RoleName;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.security.SecurityUtil;

@RequestMapping({ "/", "/index", "home" })
@Controller
public class HomeController {

	@Autowired
	Security securite ;
	
	@RequestMapping(method = RequestMethod.GET)
	public String selectPage(Model uiModel) {
		UserNomade nomade = securite.getUserNomade();

		if (nomade != null) {
			Set<RoleName> roleNames = nomade.getRoleNames();
			if (roleNames.contains(RoleName.ROLE_ADMIN)) {
				return "redirect:/admin";
			}
			if (roleNames.contains(RoleName.ROLE_SIMPLE_USER)) {

				return "redirect:/users/private/new";
			}

		}
		
		return "login";
	}

}
