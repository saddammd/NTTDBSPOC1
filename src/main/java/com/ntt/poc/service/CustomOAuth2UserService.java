//package com.ntt.poc.service;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.logging.Logger;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import com.ntt.poc.entities.Roles;
//import com.ntt.poc.entities.User;
//import com.ntt.poc.enums.Provider;
//import com.ntt.poc.filters.AuthorizationFilter;
//import com.ntt.poc.repository.Roles_Repository;
//import com.ntt.poc.repository.User_Repository;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//	@Autowired
//	User_Repository user_Repository;
//	
//	@Autowired
//	Roles_Repository roles_Repository; 
//	
//	@Autowired
//	Environment environment;
//	
//	@Autowired
//	HttpServletResponse response;
//	
//	@Autowired
//	HttpServletRequest request;
//		
//	private Logger logger = Logger.getLogger(CustomOAuth2UserService.class.getName());
//	
//	public CustomOAuth2UserService() {
//	}
//
//	@Override
//	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		
//		
//		
//		OAuth2User user = super.loadUser(userRequest);
//		User user2 = new User();
//		user2.setEmail(user.getAttributes().get("email").toString());
//		user2.setName(user.getAttributes().get("email").toString());
//		user2.setPassword("");
//		user2.setMobile("");
//		
//		user2.setProvider(Provider.GOOGLE);
//		Roles role1 = this.roles_Repository.getRoleByRoleId(1);
//		user2.getRoles().add(role1);
//		this.user_Repository.save(user2);
//		try {
//			generateToken(user2, this.request, this.response);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//       
//		System.out.println(user.getAttributes().get("email"));
//		return new CustomOAuth2User(user);
//
//	}
//
//	public String generateToken(User user, HttpServletRequest request, HttpServletResponse response)
//			throws IOException, ServletException {
//		Map<String, Object> claims = new HashMap<>();
//		Set<String> Userroles = new HashSet<>();
//		User finduser = this.user_Repository.findByEmail(user.getEmail());
//		for (Roles role : finduser.getRoles()) {
//			Userroles.add(role.getRoleName());
//		}
//		claims.put("Role", Userroles.toArray());
//		
//		return genearteJWTToken(claims, request, response, user);
//	}
//
//	private String genearteJWTToken(Map<String, Object> claims, HttpServletRequest request,
//			HttpServletResponse response, User user) throws IOException, ServletException {
//
//		String username = user.getEmail();
//		User user2 = this.user_Repository.findByEmail(username);
//		logger.info("******************************");
//		logger.info(this.environment.getProperty("token.value"));
//		logger.info("******************************");
//
//		String token = Jwts.builder()
//
//				.setClaims(claims).setSubject(user.getUserId().toString())
//				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong("300000")))
//				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.value"))
//				// .setClaims("role", claims.get("Roles"))
//				.compact();
//
//		HttpHeaders headers = new HttpHeaders();
//		
//		
//		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " +token);
//		headers.set("userId", user.getUserId().toString());
//		
//		this.request.setAttribute("headers", "Bearer " +token);
//		
//		
//		System.out.println("header " +this.request.getAttribute("Header"));
//		
//		response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " +token);
//		response.addHeader("userId", user.getUserId().toString());
//		
//
//		logger.info("*********************************************");
//		logger.info("Successful Authentication");
//		logger.info("**********************************************");
//		logger.info("Generated JWT = " + token);
//		
//		return token;
//
//}
//}