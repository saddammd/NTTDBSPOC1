package com.ntt.poc1.IntegrationTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.ntt.poc.Poc1Application;
import com.ntt.poc.dto.Logindto;
import com.ntt.poc.entities.AddProducts;
import com.ntt.poc.entities.AddRetailers;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.entities.Roles;
import com.ntt.poc.entities.User;

@SpringBootTest(classes = { Poc1Application.class })
@TestMethodOrder(OrderAnnotation.class)
public class IntegrationTest {

	@Autowired
	private RestTemplate restTemplate;
		
	private static String token;
	
	@Test
	@Order(1)
	//@WithMockUser(username = "user1", roles = "ADMIN")
	public void getLogin_Test() throws JsonProcessingException {
		
		Logindto login = new Logindto();
		login.setUsername("admin1@gmail.com");
		login.setPassword("Password@123");
		ObjectMapper mapper = new ObjectMapper();
		String LoginValueAsString = mapper.writeValueAsString(login);
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		
		
	    HttpEntity <String> entity = new HttpEntity<String>(LoginValueAsString, headers);
	    
	    String body = restTemplate.exchange("http://localhost:5000/poc/login", HttpMethod.POST, entity, String.class).getBody();
	    token = body;
	    assertNotNull(body);
	}
	
	@Test
	@Order(2)
	public void getProducts() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		String body = restTemplate.exchange(URL.PRODUCTS, HttpMethod.GET, entity, String.class).getBody();
		assertNotNull(body);
	}
	
	@Test
	@Order(3)
	public void saveProduct_Test() throws JsonProcessingException, JSONException {
		Products products = new Products();
		HttpHeaders headers = getHeaders();
		headers.set("Authorization", token);
		
		AddProducts addproducts = new AddProducts();
		//addproducts.setId(1);
		addproducts.setProductName("TestIT");
		addproducts.setCategory("CategoryTestIT");
		addproducts.setPrice(100);
		
		ObjectMapper mapper = new ObjectMapper();
		String writeAddProductsValueAsString = mapper.writeValueAsString(addproducts);
		
		HttpEntity<String> entity = new HttpEntity<String>(writeAddProductsValueAsString, headers);
		
		ResponseEntity <String> body = restTemplate.exchange(URL.PRODUCTS, HttpMethod.POST, entity, String.class);
		assertEquals(body.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@Order(4)
	public void updateProduct_Test() throws JsonProcessingException, JSONException {
		AddProducts products = new AddProducts();
		HttpHeaders headers = getHeaders();
		headers.set("Authorization", token);
		
		AddProducts addproducts = new AddProducts();
		addproducts.setProductName("TestIT");
		addproducts.setCategory("CategoryTestIT");
		addproducts.setPrice(100);
		
		ObjectMapper mapper = new ObjectMapper();
		String writeAddProductsValueAsString = mapper.writeValueAsString(addproducts);
		
		HttpEntity<String> entity = new HttpEntity<String>(writeAddProductsValueAsString, headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.PRODUCTS, HttpMethod.PUT, entity, String.class);
		assertEquals(body.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@Order(5)
	public void deleteProducts() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.PRODUCTS +"/22", HttpMethod.DELETE, entity, String.class);
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(6)
	public void getRetailersOfProduct() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.RETAILERS +"/1", HttpMethod.GET, entity, String.class);
		String s = body.getBody();
		Gson g = new Gson();
		
		List<Retailers> retailers = new ArrayList<>();
		
		TypeToken<ArrayList<Retailers>> type = new TypeToken<ArrayList<Retailers>>(){};
		retailers = g.fromJson(s, type.getType());
						
		System.out.println(retailers.get(0).getRetailerName());
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}
	
	
	@Test
	@Order(7)
	public void searchRetailers() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);

		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.RETAILERS +"?id=10&keyword=subiksha", HttpMethod.GET, entity, String.class);
		String s = body.getBody();
		Gson g = new Gson();
		
		List<Retailers> retailers = new ArrayList<>();
		
		TypeToken<ArrayList<Retailers>> type = new TypeToken<ArrayList<Retailers>>(){};
		retailers = g.fromJson(s, type.getType());
						
		System.out.println(retailers);
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}

	@Test
	@Order(8)
	public void getproductRetailers() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);

		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.RETAILERS +"/9", HttpMethod.GET, entity, String.class);
		String s = body.getBody();
		Gson g = new Gson();
		
		List<Retailers> retailers = new ArrayList<>();
		
		TypeToken<ArrayList<Retailers>> type = new TypeToken<ArrayList<Retailers>>(){};
		retailers = g.fromJson(s, type.getType());
						
		System.out.println(retailers);
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}

	
	@Test
	@Order(9)
	public void addRetailers_Test() throws JsonProcessingException {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		AddRetailers retailer = new AddRetailers(1, "test_retailername", "test_location", 9);
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(retailer);

		
		HttpEntity <String> entity = new HttpEntity<String>(writeValueAsString, headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.RETAILERS, HttpMethod.POST, entity, String.class);
		String s = body.getBody();
	
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}

	@Test
	@Order(10)
	public void updateRetailers_Test() throws JsonProcessingException {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		AddRetailers retailer = new AddRetailers(1, "test_retailername", "test_location", 9);
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(retailer);

		
		HttpEntity <String> entity = new HttpEntity<String>(writeValueAsString, headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.RETAILERS, HttpMethod.PUT, entity, String.class);
		String s = body.getBody();
	
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}

	@Test
	@Order(11)
	public void deleteRetailers_Test() throws JsonProcessingException {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.RETAILERS +"/1", HttpMethod.DELETE, entity, String.class);
		String s = body.getBody();
	
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}

	@Test
	@Order(12)
	public void showUsers() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		String body = restTemplate.exchange(URL.SHOW_USERS, HttpMethod.GET, entity, String.class).getBody();
		assertNotNull(body);
	}
	
	@Test
	@Order(13)
	public void registerUser_Test() throws JsonProcessingException, JSONException {
		User user = new User();
		HttpHeaders headers = getHeaders();
		
		user.setEmail("testEmail1");
		user.setMobile("9494949494");
		user.setName("testName1");
		user.setPassword("Password@123");
		
				
		ObjectMapper mapper = new ObjectMapper();
		String writeUserValueAsString = mapper.writeValueAsString(user);
		
		HttpEntity<String> entity = new HttpEntity<String>(writeUserValueAsString, headers);
		
		ResponseEntity <String> body = restTemplate.exchange(URL.REGISTER, HttpMethod.POST, entity, String.class);
		assertEquals(body.getStatusCode(), HttpStatus.CREATED);
	}
	
	@Test
	@Order(14)
	public void deleteUser_Test() throws JsonProcessingException {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.USER +"/56", HttpMethod.DELETE, entity, String.class);
		String s = body.getBody();
	
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}
	
	@Test
	@Order(15)
	public void getUserRoles() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.USER_ROLES +"/admin1@gmail.com", HttpMethod.GET, entity, String.class);
		String s = body.getBody();
		Gson g = new Gson();
		
		List<Roles> roles = new ArrayList<>();
		
		TypeToken<ArrayList<Roles>> type = new TypeToken<ArrayList<Roles>>(){};
		roles = g.fromJson(s, type.getType());
						
		System.out.println(roles.get(0).getRoleName());
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}
	
	@Test
	@Order(16)
	public void editUserRoles() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		String roleIds = "1";
		
		HttpEntity <String> entity = new HttpEntity<String>(roleIds, headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.USER_ROLES +"/testEmail", HttpMethod.PUT, entity, String.class);
		String s = body.getBody();
		Gson g = new Gson();
				
		User user = g.fromJson(s, User.class);
						
		System.out.println(user);
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}
	
	@Test
	@Order(17)
	public void getUser() {
		
		HttpHeaders headers = new HttpHeaders();
		headers = getHeaders();
		headers.set("Authorization", token);
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> body = restTemplate.exchange(URL.SHOW_USER +"/53", HttpMethod.GET, entity, String.class);
		
		assertEquals(HttpStatus.OK, body.getStatusCode());
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}
}
