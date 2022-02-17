package com.ntt.poc.Poc1.Controllers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.file.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.poc.controllers.Register_Controller;
import com.ntt.poc.dto.Logindto;
import com.ntt.poc.entities.Roles;
import com.ntt.poc.entities.RolesResponse;
import com.ntt.poc.entities.User;
import com.ntt.poc.entities.UserResponse;
import com.ntt.poc.service.User_Service;

@SpringBootTest(classes = { Register_ControllerTest.class })
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@ContextConfiguration
public class Register_ControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Mock
	private User_Service user_Service;
	
	@InjectMocks
	private Register_Controller register_Controller;
	
	@BeforeEach
	public void setUp() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(register_Controller).build();
	}
	
	@Test
	@Order(1)
		public void showUsers_Test() throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(getUsersList());
		when(user_Service.showAllUsers()).thenReturn(getUsersList());
		
		this.mockMvc.perform(get("/poc/showusers")).andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[0].email", Matchers.equalTo("Testemail")))
		.andDo(print());
		//assertEquals(getUsersResponseList(), register_Controller.showUsers());
	}

	
	@Test
	@Order(2)
	public void registerUser_Test() throws Exception {
		
		User user = new User("Testemail", "Testpassword", "Testname");
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(user);
		
		when(user_Service.registerUser(any())).thenReturn(user);
		this.mockMvc.perform(post("/poc/register").
				content(writeValueAsString).
				contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", Matchers.equalTo("Testname")))
		.andDo(print());
	}
	
	@Test
	@Order(3)
	public void login_Test() throws Exception {
		
		Logindto logindto = new Logindto();
		logindto.setUsername("username");
		logindto.setPassword("password@123");
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(logindto);
		this.mockMvc.perform(post("/poc/login").
				content(writeValueAsString).
				contentType(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk())
		.andDo(print());	
	}
	
	@Test
	@Order(4)
	public void deleteUser_Test() throws Exception {
		
		Integer id =1;
		user_Service.deleteUser(id);
		verify(user_Service, times(1)).deleteUser(id);
		this.mockMvc.perform(delete("/poc/user/{id}", id)).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	@Order(5)
	public void getUserRoles_Test() throws Exception {
		User user = new User("Testemail", "Testpassword", "Testname");
		Roles RolesResponse = new Roles();
		RolesResponse.setRoleId(1);
		RolesResponse.setRoleName("Admin");
		List<Roles> listRoles = new ArrayList<>();
		listRoles.add(RolesResponse);		
		user.setRoles(listRoles);
		
		
		System.out.println("print json value of user" +user);
		when(user_Service.getUser(user.getEmail())).thenReturn(user);
		this.mockMvc.perform(get("/poc/userRoles/{email}", user.getEmail()).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].roleName", Matchers.equalTo("Admin")))
		.andDo(print());
	}
	
	@Test
	@Order(6)
	public void editUserRoles_Test() throws Exception {
		
		User user = new User("Testemail", "Testpassword", "Testname");
		Roles RolesResponse = new Roles();
		RolesResponse.setRoleId(1);
		RolesResponse.setRoleName("Admin");
		List<Roles> listRoles = new ArrayList<>();
		listRoles.add(RolesResponse);		
		user.setRoles(listRoles);
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(user.getRoles().get(0).getRoleId());
		
		when(user_Service.updateRoles(any(), any())).thenReturn(user);
		this.mockMvc.perform(put("/poc/userRoles/{email}", user.getEmail()).
				content(writeValueAsString).
				contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).
		andExpect(jsonPath("$.email", Matchers.equalTo("Testemail")))
		.andDo(print());
	}
	
	@Test
	@Order(7)
	public void getUser_Test() throws Exception {
		Integer id =1;
		User user1 = new User("Testemail", "Testpassword", "Testname");
		when(user_Service.getUser(1)).thenReturn(user1);
		this.mockMvc.perform(get("/poc/showuser/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(jsonPath("$.name", Matchers.equalTo("Testname"))).andDo(print());
	}
	
private List<User> getUsersList(){
		
	User User1 = new User("Testemail", "Testpassword", "Testname");
	User User2 = new User("Testemail", "Testpassword", "Testname");
	User User3 = new User("Testemail", "Testpassword", "Testname");
		ArrayList<User> userList = new ArrayList<>();
		userList.add(User1);
		userList.add(User2);
		userList.add(User3);
		return userList;
	}
}
