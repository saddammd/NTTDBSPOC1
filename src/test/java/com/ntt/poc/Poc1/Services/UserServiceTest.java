package com.ntt.poc.Poc1.Services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ntt.poc.repository.Product_Repository;
import com.ntt.poc.repository.Retailers_Repository;
import com.ntt.poc.repository.Roles_Repository;
import com.ntt.poc.repository.User_Repository;
import com.ntt.poc.service.User_ServiceImpl;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ntt.poc.dto.Logindto;
import com.ntt.poc.entities.AddRetailers;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.entities.Roles;
import com.ntt.poc.entities.User;
import com.ntt.poc.exceptions.DuplicateRegistration;
import com.ntt.poc.exceptions.ProductNotFoundException;
import com.ntt.poc.exceptions.UserNotFoundException;
import com.ntt.poc.repository.Product_Repository;
import com.ntt.poc.repository.Retailers_Repository;
import com.ntt.poc.service.Retailers_ServiceImpl;


@SpringBootTest(classes = { RetailersServiceTest.class })
@TestMethodOrder(OrderAnnotation.class)

public class UserServiceTest {

	
	@Mock
	User_Repository user_repository;
	
	@Mock
	Environment environment;
	
	@Mock
	Roles_Repository roles_repository;
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	AuthenticationManager authenticationManager;

	@InjectMocks
	private User_ServiceImpl user_ServiceImpl;
	
	
	@Test
	@Order(1)
	public void registerUser_Test() {
		
		User User1 = new User("Testemail", "Testpassword", "Testname");
		Roles role1 = new Roles("Admin");
		when(user_repository.findByEmail(any())).thenReturn(null);
		when(roles_repository.getRoleByRoleId(1)).thenReturn(role1);
		when(bCryptPasswordEncoder.encode(any())).thenReturn("someStringValue");
		when(user_repository.save(User1)).thenReturn(User1);
		assertEquals(User1, user_ServiceImpl.registerUser(User1));
	}
	
	@Test
	@Order(2)
	public void registerUserExpception_Test() {
		
		User User1 = new User("Testemail", "Testpassword", "Testname");
		Roles role1 = new Roles("Admin");
		when(user_repository.findByEmail(any())).thenReturn(User1);
		DuplicateRegistration assertThrows2 = assertThrows(DuplicateRegistration.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {

				user_ServiceImpl.registerUser(User1);

			}

		});
		
		assertEquals("User's email address is already exist" +User1.getEmail(), assertThrows2.getMessage());
	}

	
	@Test
	@Order(3)
	public void login_Test() throws IOException, ServletException {
		
		User user = new User("Testemail", "Testpassword", "Testname");
		user.setUserId(1);
		Logindto logindto = new Logindto();
		logindto.setUsername("user1");
		logindto.setPassword("password");
		
		
		when(user_repository.findByEmail(any())).thenReturn(user);
		when(environment.getProperty("token.value")).thenReturn("encoded token value");
		
		user_ServiceImpl.login(logindto, null, response);
		verify(user_repository,times(2)).findByEmail(user.getEmail());
		
		
	}
	
	@Test
	@Order(4)
	public void loadUserByUsername_Test() {
		
		User user = new User("Testemail", "Testpassword", "Testname");
		
		when(user_repository.findByEmail(any())).thenReturn(user);
		assertEquals(new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.getRoles()), user_ServiceImpl.loadUserByUsername(user.getEmail()));
	}
	
	@Test
	@Order(5)
	public void showAllUsers_Test() {
		when(user_repository.findAll()).thenReturn(getUsersList());
		assertEquals(getUsersList(), user_ServiceImpl.showAllUsers());
	}
	
	@Test
	@Order(6)
	public void deleteUser_Test() {
		
		User user = new User("Testemail", "Testpassword", "Testname");
		user.setUserId(1);
		when(user_repository.findByUserId(1)).thenReturn(user);
		when(roles_repository.saveAll(any())).thenReturn(getRolesList());
		user_ServiceImpl.deleteUser(user.getUserId());
		verify(user_repository, times(1)).deleteByUserId(user.getUserId());
	}
	
	
	@Test
	@Order(7)
	public void showRoles_Test()
	{
		assertEquals(null, user_ServiceImpl.showRoles());
	}

	
	@Test
	@Order(8)
	public void showRoles2_Test()
	{
		assertEquals(null, user_ServiceImpl.showRoles(1));
	}
	
	@Test
	@Order(9)
	public void getRole_Test() {
		assertEquals(null, user_ServiceImpl.getRole(1));
	}
	
	@Test
	@Order(10)
	public void getUser_Test() {
		
		User user = new User("Testemail", "Testpassword", "Testname");
		when(user_repository.findByUserId(any())).thenReturn(user);
		assertEquals(user, user_ServiceImpl.getUser(1));
	}
	
	@Test
	@Order(11)
	public void getUser1_Test() {
		
		User user = new User("Testemail", "Testpassword", "Testname");
		when(user_repository.findByEmail(any())).thenReturn(user);
		assertEquals(user, user_ServiceImpl.getUser(user.getEmail()));
	}
	
	@Test
	@Order(12)
	public void updateRoles_Test() {
		User user = new User("Testemail", "Testpassword", "Testname");
		Roles role = new Roles("Admin");
		role.setRoleId(1);
		when(user_repository.findByEmail(any())).thenReturn(user);
		when(user_repository.save(any())).thenReturn(user);
		when(roles_repository.getRoleByRoleId(any())).thenReturn(role);
		assertEquals(user, user_ServiceImpl.updateRoles(user.getEmail(), role.getRoleId().toString()));
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
	
	private List<Roles>getRolesList(){
		
		Roles role1 = new Roles("Admin");
		Roles role2 = new Roles("SuperUser");
		Roles role3 = new Roles("User");
		List<Roles> roles = new ArrayList<>();
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		return roles;
		}
}
