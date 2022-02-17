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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.poc.controllers.Products_Controller;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.ProductsResponse;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.service.Products_ServiceImpl;

@SpringBootTest(classes = { Products_ControllerTest.class })
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@ContextConfiguration
public class Products_ControllerTest {

	
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	private Products_ServiceImpl products_ServiceImpl;
	
	@InjectMocks
	private Products_Controller products_Controller;
	
	@BeforeEach
	public void setUp() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(products_Controller).build();
	}
	
	@Test
	@Order(1)
	@WithMockUser(username = "user1", roles = "ADMIN")
	public void listProduct_Test() throws Exception {
		
		int pageSize = 100;
		int pageNo = 1;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
						
		when(products_ServiceImpl.searchProducts("", pageable)).thenReturn(getList());
		this.mockMvc.perform(get("/poc/products").param("keyword", "").
				param("pageno", "1").param("pageSize", "100")).andExpect(status().isOk()).andDo(print());
	}
	
	
	@Test
	@Order(2)
	@WithMockUser(username = "user1", roles = "ADMIN")
	public void listProductWithKeyword_Test() throws Exception {
		
		int pageSize = 100;
		int pageNo = 1;
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		List<Products> list = getList();
						
		when(products_ServiceImpl.searchProducts("", pageable)).thenReturn(list);
		ResponseEntity<List<ProductsResponse>> listProducts = products_Controller.listProducts("", pageNo, pageSize);
		assertEquals(HttpStatus.OK, listProducts.getStatusCode());
		assertEquals(3,listProducts.getBody().size());
		
//		this.mockMvc.perform(get("/poc/products").param("keyword", "test").
//			param("pageno", "1").param("pageSize", "100"))
//		.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.[1].productName").value(getList().get(1).getProductName()));
	}
	
	@Test
	@Order(3)
	public void saveProduct_Test() throws Exception {
		
		Products product1 = new Products("TestProductName1", 100, "TestProductCategory1", getRetailers());
		when(products_ServiceImpl.saveProducts(product1)).thenReturn(product1);
		
		ObjectMapper map = new ObjectMapper();
		String JsonProductBody = map.writeValueAsString(product1);
		
		this.mockMvc.perform(post("/poc/products").content(JsonProductBody).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andDo(print());
		
	}
	
	@Test
	@Order(4)
	public void updateProduct_Test() throws Exception {
		
		Products product1 = new Products("TestProductName1", 100, "TestProductCategory1", getRetailers());
		when(products_ServiceImpl.saveProducts(product1)).thenReturn(product1);
		
		ObjectMapper map = new ObjectMapper();
		String JsonProductBody = map.writeValueAsString(product1);
		
		this.mockMvc.perform(put("/poc/products").content(JsonProductBody).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andDo(print());
		
	}
	
	@Test
	@Order(5)
	public void deleteProduct_Test() throws Exception {
		
		Integer id = 1;
		
		products_ServiceImpl.deleteProducts(id);
		verify(products_ServiceImpl, times(1)).deleteProducts(id);
		this.mockMvc.perform(delete("/poc/products/{id}", id)).andExpect(status().isOk()).andDo(print());
		
	}
	
	@Test
	@Order(6)
	public void getRetailers_Test() throws Exception {
		
		Integer id= 1;
		when(products_ServiceImpl.getRetailers(id)).thenReturn(getRetailers());
		this.mockMvc.perform(get("/poc/retailers/{id}", id)).andExpect(status().isOk()).andDo(print());
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
















