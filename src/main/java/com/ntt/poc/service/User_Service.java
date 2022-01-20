package com.ntt.poc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ntt.poc.dto.Logindto;
import com.ntt.poc.entities.Roles;
import com.ntt.poc.entities.UpdateRoles;
import com.ntt.poc.entities.User;


public interface User_Service extends UserDetailsService {
	
	public User registerUser(User user);
	public void login (Logindto logindto, HttpServletRequest request, HttpServletResponse response);
	public List<User> showAllUsers();
	public void deleteUser(Integer id);
	public List<Roles> showRoles(Integer userid);
	public List<Roles> showRoles();
	public User updateRoles(String email, String roleIds);
	public Roles getRole(Integer id);
	public User getUser(Integer userId);
	public User getUser(String email);
	
	

}
