package com.nomade.appinit;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Used to initialize the application, creating the defaul user, aso.
 * 
 * @author guymoyo
 *
 */
@Service("applicationInitFilter")
public class ApplicationInitFilter extends OncePerRequestFilter {

	@Resource
	private ApplicationInitService applicationInitService;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private boolean initialized;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (!initialized){
			mongoTemplate.getDb().dropDatabase();
			applicationInitService.initApplication();
			initialized = true;
		}
		
		filterChain.doFilter(request, response);
	}

}
