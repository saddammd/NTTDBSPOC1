package com.ntt.poc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.poc.dto.Logindto;
import com.ntt.poc.entities.Roles;
import com.ntt.poc.entities.RolesResponse;
import com.ntt.poc.entities.UpdateRoles;
import com.ntt.poc.entities.User;
import com.ntt.poc.entities.UserResponse;
import com.ntt.poc.entities.User_Response;
import com.ntt.poc.service.User_Service;

@CrossOrigin
@RestController
@RequestMapping("/poc")
public class Register_Controller {

	@Autowired
	public User_Service user_Service;
	
	org.slf4j.Logger logger = LoggerFactory.getLogger(Register_Controller.class);

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/showusers")
	public ResponseEntity<List<UserResponse>> showUsers() {

		List<User> showAllUsers = this.user_Service.showAllUsers();
		List<UserResponse> show = new ArrayList<>();
		logger.info("show all users called");
		for (User u : showAllUsers) {

			UserResponse target = new UserResponse();
			BeanUtils.copyProperties(u, target);
			show.add(target);
		}
		logger.info("show all users completed");
		return new ResponseEntity(show, HttpStatus.OK);
		//adding comment
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		logger.info("registration request received");
		User registerUser = user_Service.registerUser(user);
		logger.info("registration request completed");
		return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);

	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Logindto logindto, HttpServletRequest request,
			HttpServletResponse response) {
		user_Service.login(logindto, request, response);
		logger.info("login request received");
		String token = response.getHeader("token");
		logger.info("login request completed");
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/user/{id}")
	public ResponseEntity deleteUser(@PathVariable Integer id) {
		logger.info("delete request received");
		this.user_Service.deleteUser(id);
		logger.info("delete request completed");
		return new ResponseEntity(HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN', 'USER')")
	@GetMapping("/userRoles/{email}")
	public ResponseEntity<List<RolesResponse>> getUserRoles(@PathVariable String email) {
		logger.info("get roles request received");
		User user = this.user_Service.getUser(email);
		User_Response ur = new User_Response();
		BeanUtils.copyProperties(user, ur);
		List<Roles> roles = ur.getRoles();
		List<RolesResponse> rolesResonseList = new ArrayList<>();
		for (Roles r : roles) {
			
			RolesResponse rolesResponse = new RolesResponse();
			BeanUtils.copyProperties(r, rolesResponse);
			rolesResonseList.add(rolesResponse);
			
		}
		logger.info("get roles request completed");
		return ResponseEntity.status(HttpStatus.OK).body(rolesResonseList);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/userRoles/{email}")
	public User editUserRoles(@PathVariable String email, @RequestBody String roleIds){
		logger.info("edit role request received");
		return this.user_Service.updateRoles(email, roleIds);
		
	}
	
	@GetMapping("/showuser/{id}")
	public User getUser(@PathVariable Integer id) {
		logger.info("get user details received");
	return this.user_Service.getUser(id);
		 
	}
  
}