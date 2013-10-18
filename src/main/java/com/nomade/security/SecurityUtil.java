package com.nomade.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.nomade.domain.UserNomade;
import com.nomade.service.UserService;

@Configurable
public class SecurityUtil {

	@Autowired
	UserService userService;

	public static UserDetails getUserDetails() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null)
			return null;
		Authentication authentication = context.getAuthentication();
		if (authentication == null)
			return null;
		Object principal = authentication.getPrincipal();
		if (principal == null)
			return null;
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal);
		} else {
			return null;
		}
	}

	public static String getUserName() {
		UserDetails userDetails = getUserDetails();
		if (userDetails != null)
			return userDetails.getUsername();
		return null;
	}

	public UserNomade getUserNomade() {
		UserDetails userDetails = getUserDetails();
		if (userDetails == null)
			return null;
		String username = userDetails.getUsername();
		List<UserNomade> resultList = userService.findByUserName(username);
		if (resultList != null && resultList.size() > 0) {

			return resultList.get(0);
		} else {
			List<UserNomade> resultList1 = userService.findByEmail(username);
			return resultList1.get(0);
		}
	}

	public SecurityUtil() {
		super();
	}
	

}
