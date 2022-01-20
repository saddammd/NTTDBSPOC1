package com.ntt.poc.entities;

import java.util.ArrayList;
import java.util.List;

public class User_Response {
	
	private List<Roles> roles = new ArrayList<>();
				
	public User_Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<Roles> getRoles() {
		return roles;
	}
	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
	
	

}
