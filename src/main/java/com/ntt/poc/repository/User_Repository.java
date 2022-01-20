package com.ntt.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntt.poc.entities.User;

public interface User_Repository extends JpaRepository<User, String> {
	
	public User findByEmail(String username);
	public User findByUserId(Integer userId);
		
	public void deleteByUserId(Integer userId);

}
