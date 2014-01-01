/**
 * 
 */
package com.nomade.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import com.nomade.domain.UserNomade;

/**
 * @author guymoyo
 * 
 */
@Service(value = "authentificationHandler")
public class AuthentificationHandler extends
		SimpleUrlAuthenticationSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {

		int fieldPercent = getUserNomade(authentication).fieldPercent();
		HttpSession session = request.getSession();
		session.setAttribute("fieldPercent", fieldPercent);
		
		UserDetails userDetails = getUserDetails(authentication);
		Cookie myCookie = new Cookie("jforumUserInfo", userDetails.getUsername());
		myCookie.setPath("/");
		response.addCookie(myCookie);
		
		//response.
		super.onAuthenticationSuccess(request, response, authentication);
		// response.sendRedirect(contextPath);

	}

	public UserDetails getUserDetails(Authentication authentication) {

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

	public UserNomade getUserNomade(Authentication authentication) {
		UserDetails userDetails = getUserDetails(authentication);
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

	/*
	 * (non-Javadoc)@Override public void onAuthenticationFailure
	 * 
	 * @see
	 * org.springframework.security.web.authentication.logout.LogoutSuccessHandler
	 * #onLogoutSuccess(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		System.out.println("logout");

		response.sendRedirect(request.getContextPath() + "/login");

	}

}
