package com.ntt.poc.entities;

public class RolesResponse {
	
	private Integer roleId;
	private String roleName;
	private String authority;
	
	public RolesResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	public RolesResponse(String roleName) {
		super();
		this.roleName = roleName;
	}

	public Integer getRoleId() {
		return roleId;
	}


	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}




	public String getAuthority() {
		return authority;
	}


	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	

}
