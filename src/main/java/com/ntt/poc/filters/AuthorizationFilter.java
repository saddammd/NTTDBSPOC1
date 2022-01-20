package com.ntt.poc.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.ntt.poc.utilities.AuthenticationUtility;

public class AuthorizationFilter extends BasicAuthenticationFilter {
			
	private AuthenticationManager authenticationManager;
	org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	AuthenticationUtility authenticationUtility;
		
	public AuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationUtility authenticationUtility) {
		super(authenticationManager);
		this.authenticationManager = authenticationManager;
		this.authenticationUtility = authenticationUtility;

	}
			

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		
		logger.info(request.getHeader(HttpHeaders.AUTHORIZATION));
				
		if ((request.getHeader(HttpHeaders.AUTHORIZATION) != null) &&
			(!request.getHeader(HttpHeaders.AUTHORIZATION).equals("Bearer null"))) {

			try {
			UsernamePasswordAuthenticationToken authentication = authenticationUtility.getAuthentication(request);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
			} catch(IllegalArgumentException e) {
				UsernamePasswordAuthenticationToken googleAuthentication = authenticationUtility.getGoogleAuthentication(request);		
				SecurityContextHolder.getContext().setAuthentication(googleAuthentication);
				chain.doFilter(request, response);
			}
		} 
		
		else {
			chain.doFilter(request, response);
		}
	}

}