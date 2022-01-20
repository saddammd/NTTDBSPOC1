//package com.ntt.poc.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.ntt.poc.entities.User;
//import com.ntt.poc.repository.User_Repository;
//
//@Service
//public class Security_Service implements UserDetailsService {
//	
//	@Autowired
//	User_Repository user_repository;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User findByUsername = user_repository.findByEmail(username);
//		return new org.springframework.security.core.userdetails.User(
//				findByUsername.getEmail(),
//				findByUsername.getPassword(), 
//				findByUsername.getRoles());
//	}
//}
