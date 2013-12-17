package com.nomade.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nomade.domain.BeanRegister;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;


public class LoginPathInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	Security securite ;
	
	@Override
    public boolean preHandle(HttpServletRequest request,
    HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI().substring(request.getContextPath().length());
 
        if ("/login".equals(requestUrl) || requestUrl.contains("/login")) {
        	request.setAttribute("beanRegister", new BeanRegister());
        }
        	
        return true;        
    }
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
		UserNomade userNomade = securite.getUserNomade();
		if(userNomade!=null){
			modelAndView.addObject("nomade", userNomade);
		}
		
		super.postHandle(request, response, handler, modelAndView);
	}

}
