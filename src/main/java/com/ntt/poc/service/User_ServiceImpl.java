package com.ntt.poc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntt.poc.dto.Logindto;
import com.ntt.poc.entities.Roles;
import com.ntt.poc.entities.User;
import com.ntt.poc.exceptions.DuplicateRegistration;
import com.ntt.poc.repository.Roles_Repository;
import com.ntt.poc.repository.User_Repository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Transactional
public class User_ServiceImpl implements User_Service   {

	@Autowired
	private User_Repository user_repository;
	private Roles_Repository roles_repository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private AuthenticationManager authenticationManager;
	private Environment environment;
	

	private Logger logger = Logger.getLogger(User_ServiceImpl.class.getName());

	public User_ServiceImpl(User_Repository user_repository, Roles_Repository roles_repository,
			BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager,
			Environment environment) {
		super();
		this.user_repository = user_repository;
		this.roles_repository = roles_repository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.authenticationManager = authenticationManager;
		this.environment = environment;

	}

	@Override
	public User registerUser(User user) {

		if(this.user_repository.findByEmail(user.getEmail())==null) {
		Roles role1 = roles_repository.getRoleByRoleId(1);
		user.getRoles().add(role1);
		
		String password_encode = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(password_encode);
		
		return user_repository.save(user);		
		}
		throw new DuplicateRegistration("User's email address is already exist" +user.getEmail());
		
	}

	@Override
	public void login(Logindto logindto, HttpServletRequest request, HttpServletResponse response) {

		UserDetails userDetails2 = loadUserByUsername(logindto.getUsername());

		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails2,
				logindto.getPassword(), userDetails2.getAuthorities());
		try {
		authenticationManager.authenticate(token);
		}catch(Exception e) {
			throw new UsernameNotFoundException("Bad Credentials");
		}
		boolean result = token.isAuthenticated();
		if (result) {
			SecurityContextHolder.getContext().setAuthentication(token);

			try {
				String jwtToken = generateToken(userDetails2, request, response);

			} catch (IOException | ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = user_repository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username + "User Not Found");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.getRoles());
	}

	public String generateToken(UserDetails userDetails, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Map<String, Object> claims = new HashMap<>();
		Set<String> Userroles = new HashSet<>();
		User user = user_repository.findByEmail(userDetails.getUsername());
		for (Roles role : user.getRoles()) {
			Userroles.add(role.getRoleName());
		}
		claims.put("Role", Userroles.toArray());
		return genearteJWTToken(claims, request, response, userDetails);
	}

	private String genearteJWTToken(Map<String, Object> claims, HttpServletRequest request,
			HttpServletResponse response, UserDetails userDetails) throws IOException, ServletException {

		String username = userDetails.getUsername();
		User user = user_repository.findByEmail(username);
		logger.info("******************************");
		logger.info(this.environment.getProperty("token.value"));
		logger.info("******************************");

		String token = Jwts.builder()

				.setClaims(claims).setSubject(user.getUserId().toString())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong("300000")))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.value"))
				// .setClaims("role", claims.get("Roles"))
				.compact();

		response.addHeader("token", token);
		response.addHeader("userId", user.getUserId().toString());
		// response.addHeader("roles", claims.toString());

		logger.info("*********************************************");
		logger.info("Successful Authentication");
		logger.info("**********************************************");
		logger.info("Generated JWT = " + token);

		return token;

		// super.successfulAuthentication(request, response, chain, authResult);
	}

	@Override
	public List<User> showAllUsers() {
		return this.user_repository.findAll();
	}

	@Override
	public void deleteUser(Integer id) {
		
		User findByUserId = this.user_repository.findByUserId(id);
		List<Roles> roles = findByUserId.getRoles();
		roles.remove(findByUserId);
		this.roles_repository.saveAll(roles);
		this.user_repository.deleteByUserId(findByUserId.getUserId());
		
	}

	@Override
	public List<Roles> showRoles(Integer userid) {
		return null;
	}

	@Override
	public List<Roles> showRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Roles getRole(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(Integer userId) {
		return this.user_repository.findByUserId(userId);
	}
	
	@Override
	public User getUser(String email) {
		return this.user_repository.findByEmail(email);
	}

	@Override
	public User updateRoles(String email, String roleIds) {
		User user = this.user_repository.findByEmail(email);
		List<Roles>removeRoles = new ArrayList<>();
		removeRoles = user.getRoles();
		user.getRoles().removeAll(removeRoles);
		this.user_repository.save(user);
		
		User user2 = this.user_repository.findByEmail(email);
		String rolesId[] = roleIds.trim().split(",");
		List<Roles> rolesList = new ArrayList<>();
		Integer value = 0;
		for(String s:rolesId) {
			if(s!="0") {
				value = Integer.valueOf(s);
				rolesList.add(roles_repository.getRoleByRoleId(value)); 
				}
} 
		user2.getRoles().addAll(rolesList);
		this.user_repository.save(user2);
			 
		return this.user_repository.save(user2);
	}
	

}
