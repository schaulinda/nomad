package com.nomade.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nomade.domain.BeanRegister;


public class LoginPathInterceptor extends HandlerInterceptorAdapter {
	@Override
    public boolean preHandle(HttpServletRequest request,
    HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI().substring(request.getContextPath().length());
 
        if ("/login".equals(requestUrl) || requestUrl.contains("/login")) {
        	request.setAttribute("beanRegister", new BeanRegister());
        }
        	
        return true;        
    }

}
