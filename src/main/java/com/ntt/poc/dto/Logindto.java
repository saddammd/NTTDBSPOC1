package com.ntt.poc.dto;

public class Logindto {
	
	private String username;
	private String password;
	
	public Logindto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Logindto(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
