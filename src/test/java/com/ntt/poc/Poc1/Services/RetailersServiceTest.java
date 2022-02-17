package com.ntt.poc.Poc1.Services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ntt.poc.entities.AddRetailers;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.exceptions.UserNotFoundException;
import com.ntt.poc.repository.Product_Repository;
import com.ntt.poc.repository.Retailers_Repository;
import com.ntt.poc.service.Retailers_ServiceImpl;

@SpringBootTest(classes = { RetailersServiceTest.class })
@TestMethodOrder(OrderAnnotation.class)

public class RetailersServiceTest {

	
	@Mock
	Product_Repository product_Repository;
	
	@Mock
	Retailers_Repository retailers_Repository;

	@InjectMocks
	private Retailers_ServiceImpl retailers_ServiceImpl;
	
	
	@Test
	@Order(1)
	public void findAllRetailers_Test() {
		
		when(retailers_Repository.findByProductsId(1)).thenReturn(getRetailers());
		assertEquals(getRetailers(), retailers_ServiceImpl.findAll(1));
	}
	
	@Test
	@Order(2)
	public void saveRetailers_Test() {
		
		Retailers retailer1 = new Retailers(1, "TestRetailerName1", "Test_Location1");
		AddRetailers AddRetailers = new AddRetailers(1, "TestRetailerName1", "Test_Location1", 1);
		Products product1 = new Products("TestProductName1", 100, "TestProductCategory1", getRetailers());
		product1.setId(1);
		retailer1.setProducts(product1);
		
		retailer1.getProducts().setId(AddRetailers.getProducts());
		when(product_Repository.getById(1)).thenReturn(product1);
		when(retailers_Repository.save(any())).thenReturn(retailer1);
		assertEquals(retailer1, retailers_ServiceImpl.saveRetailer(AddRetailers));
		
	}
	
	@Test
	@Order(3)
	public void deleteRetailer_Test() throws UserNotFoundException {
		retailers_ServiceImpl.deleteRetailer(1);
		verify(retailers_Repository, times(1)).deleteById(1);
	}
	
	@Test
	@Order(4)
	public void getRetailer_Test() {
		
		Retailers retailer1 = new Retailers(1, "TestRetailerName1", "Test_Location1");
		Optional<Retailers> findById = Optional.ofNullable(retailer1);
		when(retailers_Repository.findById(any())).thenReturn(findById);
		retailers_ServiceImpl.getRetailer(1);
		assertEquals(findById, retailers_ServiceImpl.getRetailer(1));	
	}
	
	@Test
	@Order(5)
	public void searchRetailers_Test() {
		when(retailers_Repository.search(1, "Test")).thenReturn(getRetailers());
		assertEquals(getRetailers(), retailers_ServiceImpl.searchRetailers(1, "Test"));
	}
	
	@Test
	@Order(6)
	public void searchRetailers2_Test() {
		when(retailers_Repository.findByProductsId(1)).thenReturn(getRetailers());
		retailers_ServiceImpl.searchRetailers(1, "");
		assertEquals(getRetailers(), retailers_ServiceImpl.findAll(1));
	}
	
	@Test
	@Order(7)
	public void getRetailerofProduct_Test() {
		
		Retailers retailer1 = new Retailers(1, "TestRetailerName1", "Test_Location1");
		when(retailers_Repository.findByProductsIdAndRetailerId(1, 1)).thenReturn(retailer1);
		assertEquals(retailer1, retailers_ServiceImpl.getRetailer(1, 1));
		
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


















