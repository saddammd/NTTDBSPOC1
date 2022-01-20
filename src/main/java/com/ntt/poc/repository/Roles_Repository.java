package com.ntt.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntt.poc.entities.Roles;

public interface Roles_Repository extends JpaRepository<Roles, Integer> {
	
	public Roles getRoleByRoleId(Integer id);
	public Roles findByRoleId(Integer id);

}
