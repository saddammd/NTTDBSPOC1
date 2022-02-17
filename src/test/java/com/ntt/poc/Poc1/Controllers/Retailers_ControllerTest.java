package com.ntt.poc.Poc1.Controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.poc.controllers.Retailers_Controller;
import com.ntt.poc.entities.AddRetailers;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.entities.RetailersResponse;
import com.ntt.poc.exceptions.UserNotFoundException;
import com.ntt.poc.service.Retailers_Service;


@SpringBootTest(classes = { Retailers_ControllerTest.class })
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@ContextConfiguration
public class Retailers_ControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	Retailers_Service retailers_ServiceImpl;
	
	@InjectMocks
	Retailers_Controller retailers_Controller;
	
	@BeforeEach
	public void setUp() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(retailers_Controller).build();
	}
	
	@Test
	@Order(1)
	public void listRetailers_Test() {
		
		Integer id = 1;
		String keyword = "";
		when(retailers_ServiceImpl.searchRetailers(id, keyword)).thenReturn(getRetailers());
		ResponseEntity<List<RetailersResponse>> listRetailers = retailers_Controller.listRetailers(id, keyword);
		assertEquals(HttpStatus.OK, listRetailers.getStatusCode());
		assertEquals(3, listRetailers.getBody().size());
	}

	@Test
	@Order(2)
	@WithMockUser(username = "user1", roles = "ADMIN")
	public void getRetailer_Test() throws Exception {
		
		Integer productid = 1;
		Integer retailerid = 1;
		String keyword = "";
		Retailers retailer1 = new Retailers(1, "TestRetailerName1", "Test_Location1");
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(retailer1);
		
		when(retailers_ServiceImpl.getRetailer(productid, retailer1.getRetailerId())).thenReturn(retailer1);
			
		this.mockMvc.perform(get("/poc/retailers/{productid}/", productid).param("retailerid", "1").content(writeValueAsString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
	}
	
	
	@Test
	@Order(3)
	public void saveRetailer_Test() throws Exception {
		
		AddRetailers addretailer = new AddRetailers(1, "TestRetailerName1", "Test_Location1",1);
		Retailers retailer1 = new Retailers(1, "TestRetailerName1", "Test_Location1");
		when(retailers_ServiceImpl.saveRetailer(addretailer)).thenReturn(retailer1);
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(addretailer);
		
		
		this.mockMvc.perform(post("/poc/retailers").content(writeValueAsString).
				contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk()).andDo(print());
		
		
	}
	
	@Test
	@Order(4)
	public void updateRetailer_Test() throws Exception {
		
		AddRetailers addretailer = new AddRetailers(1, "TestRetailerName1", "Test_Location1",1);
		Retailers retailer1 = new Retailers(1, "TestRetailerName1", "Test_Location1");
		when(retailers_ServiceImpl.saveRetailer(addretailer)).thenReturn(retailer1);
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(addretailer);
		
		
		this.mockMvc.perform(put("/poc/retailers").content(writeValueAsString).
				contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk()).andDo(print());
		
		
	}
	
	@Test
	@Order(5)
	public void deleteProduct_test() throws Exception {
		
		Integer id = 1;
		retailers_ServiceImpl.deleteRetailer(1);
		verify(retailers_ServiceImpl, times(1)).deleteRetailer(1);
		this.mockMvc.perform(delete("/poc/retailers/{id}", id)).andExpect(status().isOk());
		
	}
	
	private List<Products> getList(){
		
		Products product1 = new Products("TestProductName1", 100, "TestProductCategory1", getRetailers());
		Products product2 = new Products("TestProductName2", 100, "TestProductCategory1", getRetailers());
		Products product3 = new Products("TestProductName3", 100, "TestProductCategory1", getRetailers());
		List<Products> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		return products;
	}
	
	private List<Retailers> getRetailers(){
		
		Retailers retailer1 = new Retailers(1, "TestRetailerName1", "Test_Location1");
		Retailers retailer2 = new Retailers(2, "TestRetailerName2", "Test_Location2");
		Retailers retailer3 = new Retailers(3, "TestRetailerName3", "Test_Location3");
		
		List<Retailers> retailers = new ArrayList<>();
		retailers.add(retailer1);
		retailers.add(retailer2);
		retailers.add(retailer3);
		return retailers;
		
	}
	
}
















