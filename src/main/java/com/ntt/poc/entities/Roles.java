package com.ntt.poc.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "Roles")
public class Roles implements GrantedAuthority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	private String roleName;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="user_roles", 
	joinColumns = @JoinColumn(name="roles_userId"), 
	inverseJoinColumns = @JoinColumn(name="users_roleId"))
	private List<User> users;
	
	
	public Roles() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	public Roles(String roleName) {
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




	@Override
	public String toString() {
		return "Roles [roleId=" + roleId + ", roleName=" + roleName + ", users=" + users + "]";
	}




	@Override
	public String getAuthority() {
		return roleName;
	}
	
	
	

}
