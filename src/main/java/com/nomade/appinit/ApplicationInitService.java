package com.nomade.appinit;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomade.domain.RoleName;
import com.nomade.domain.UserNomade;
import com.nomade.service.UserService;

@Service
@Transactional
public class ApplicationInitService {

	@Autowired
	UserService userService;
	
	public void initApplication() {
		
		Set<RoleName> roleNames = new HashSet<RoleName>();
		roleNames.add(RoleName.ROLE_SIMPLE_USER);
		UserNomade nomade = new UserNomade("toto", "titi", false, roleNames);
		userService.saveUserNomade(nomade);
		
		UserNomade nomade1 = new UserNomade("mama", "123", true, roleNames);
		userService.saveUserNomade(nomade1);
		
		UserNomade nomade2 = new UserNomade("hermine", "yougo", false, roleNames);
		nomade2.setDisableLogin(true);
		userService.saveUserNomade(nomade2);
	}

}
