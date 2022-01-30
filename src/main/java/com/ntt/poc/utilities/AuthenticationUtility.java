package com.ntt.poc.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ntt.poc.entities.Roles;
import com.ntt.poc.entities.User;
import com.ntt.poc.enums.Provider;
import com.ntt.poc.repository.Roles_Repository;
import com.ntt.poc.repository.User_Repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthenticationUtility  {

	org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${google.clientId}")
	private String clientId;
	
	@Autowired
	private User_Repository user_Repository;
	
	@Autowired
	private Roles_Repository roles_Repository;
	
	
	private Environment environment;
	
	@Autowired
	public AuthenticationUtility(Environment environment) {
		this.environment = environment;
	}

	public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

		logger.info("signed in with JWT token");
		String headers = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = headers.replace("Bearer", "");
		Claims claims = Jwts.parser().setSigningKey("1nc4jRjdO5enfUc4loN3q7gEb8fhr9O").parseClaimsJws(token).getBody();
		String id = Jwts.parser().setSigningKey("1nc4jRjdO5enfUc4loN3q7gEb8fhr9O").parseClaimsJws(token).getBody()
				.getSubject();
		logger.info("decoded JWT Token");

		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		String args[] = claims.get("Role").toString().split(",");

		logger.info("trimming roles from JWT token");
		for (String roles : args) {

			String str1 = roles.replaceAll("\\[", "").trim();
			String str2 = str1.replaceAll("\\]", "").trim();
			String role = "ROLE_".concat(str2);
			authorities.add(new SimpleGrantedAuthority(role));

			logger.info("role " +role +" has trimmed from JWT Token");
		}

		Collection<? extends GrantedAuthority> authorities2 = java.util.Arrays.asList(args).stream()
				.map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

		logger.info("passing authentication object");
		return new UsernamePasswordAuthenticationToken(claims.getSubject(),
				request.getHeader(HttpHeaders.AUTHORIZATION), authorities);

	}
	
	
	public UsernamePasswordAuthenticationToken getGoogleAuthentication(HttpServletRequest request) 
			throws IOException {
		logger.info("Signined in with Google token");
		System.out.println(environment.getProperty("google.clientId"));
		 final NetHttpTransport transport = new NetHttpTransport();
	        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
	        GoogleIdTokenVerifier.Builder verifier =
	                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
	                .setAudience(Collections.singletonList(environment.getProperty("google.clientId")));
	        
	        String headers = request.getHeader(HttpHeaders.AUTHORIZATION);
			String token = headers.replace("Bearer", "");
			logger.info("removing bearer from google token");
	        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), token);
	        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
	        
	        try {
	        logger.info("finding the user in database, if not found user will be registered");
	        if(this.user_Repository.findByEmail(payload.getEmail())==null) {
	        	logger.info("user details not found in database so proceeding with registration");
	        	logger.info(payload.getEmail().toString());
	        	User user2 = new User();
	    		user2.setEmail(payload.getEmail());
	    		user2.setName(payload.getEmail());
	    		user2.setPassword("");
	    		user2.setMobile("");
	    		 
	    		user2.setProvider(Provider.GOOGLE);
	    		Roles role1 = this.roles_Repository.getRoleByRoleId(2);
	    		user2.getRoles().add(role1);
	    		this.user_Repository.save(user2);
	    		logger.info("a new user registered with Google email Id");
	    	 }
	        }catch(Exception e) {
	        	logger.info("Exception has thrown while registering a new google user " +payload.getEmail());
	        } 
	        
	        User findUser = this.user_Repository.findByEmail(payload.getEmail());
	        logger.info("user has found in database" +payload.getEmail().toString());

	        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
	    	Roles roles = new Roles();
	    	
	    	int size = findUser.getRoles().size();
	    	logger.info("getting roles details of user");
	    		for (int i=0; i<=size-1; i++) {
	    	
	    	roles = findUser.getRoles().get(i);
	    	String str = roles.getAuthority();
	    	logger.info("received roles of user " +str);
	    	logger.info("trimming roles details from database");
	    	String str1 = str.replaceAll("\\[", "").trim();
	    	String str2 = str1.replaceAll("\\]", "").trim();
	    	String role = "ROLE_".concat(str2);
	    	authorities.add(new SimpleGrantedAuthority(role));

	    			logger.info("role value after trimming is " +role);
	    		}
	    		logger.info("allocating the user email and roles to authentication object");
	    		return new UsernamePasswordAuthenticationToken(findUser.getEmail(),
	    				request.getHeader(HttpHeaders.AUTHORIZATION), authorities);

	        }
	
	}
