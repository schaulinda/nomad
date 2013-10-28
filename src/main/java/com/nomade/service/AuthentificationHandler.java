/**
 * 
 */
package com.nomade.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;


/**
 * @author guymoyo
 *
 */
@Service(value="authentificationHandler")
public class AuthentificationHandler extends SimpleUrlAuthenticationSuccessHandler 
implements LogoutSuccessHandler{
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		
		// 
		String contextPath = request.getContextPath(); 
		System.out.println("Context Path de la requete sur logon succes: "+contextPath);
		
		
		/*super.onAuthenticationSuccess(request, response, authentication);*/
		
			super.onAuthenticationSuccess(request, response, authentication);
		    //response.sendRedirect(contextPath);			
				
	}


	
	/* (non-Javadoc)@Override
    public void onAuthenticationFailure
	 * @see org.springframework.security.web.authentication.logout.LogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
			System.out.println("logout"); 
		
		response.sendRedirect(request.getContextPath()+"/login");
		
	}

	
	
	
}
