package com.ntt.poc.Poc1.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.exceptions.ProductNotFoundException;
import com.ntt.poc.repository.Product_Repository;
import com.ntt.poc.repository.Retailers_Repository;
import com.ntt.poc.service.Products_ServiceImpl;

@SpringBootTest(classes = { ProductServiceTest.class })
@TestMethodOrder(OrderAnnotation.class)
public class ProductServiceTest {

	@Mock
	Product_Repository product_Repository;
	
	@Mock
	Retailers_Repository retailers_Repository;

	@InjectMocks
	private Products_ServiceImpl products_ServiceImpl;

	private List<Products> productsList;

	private List<Retailers> retailersList;

	@Test
	@Order(1)
	public void test_findAll() {

		Retailers retailers1 = new Retailers(1, "test1_Rakuten", "Tokyo");
		Retailers retailers2 = new Retailers(2, "test1_Seven Eleven", "Tokyo");
		productsList = new ArrayList<>();
		retailersList = new ArrayList<>();
		retailersList.add(retailers1);
		retailersList.add(retailers2);

		Products products1 = new Products(1, "test1_Nokia", 1000, "Mobile", retailersList);
		Products products2 = new Products(2, "test1_Motorola", 1000, "Mobile", retailersList);
		productsList.add(products2);
		productsList.add(products1);

		when(product_Repository.findAll()).thenReturn(productsList);
		assertEquals(productsList, products_ServiceImpl.findAll());

	}

	@Test
	@Order(2)
	public void test_saveProducts() {

		productsList = new ArrayList<>();
		retailersList = new ArrayList<>();
		Retailers retailers1 = new Retailers(1, "test2_Rakuten", "Tokyo");
		Retailers retailers2 = new Retailers(2, "test2_Seven Eleven", "Tokyo");
		retailersList.add(retailers1);
		retailersList.add(retailers2);

		Products products = new Products(1, "test2_Nokia", 1000, "Mobile", retailersList);

		when(product_Repository.save(products)).thenReturn(products);
		assertEquals(products, products_ServiceImpl.saveProducts(products));
	}

	@Test
	@Order(3)
	public void test_deleteProducts() throws ProductNotFoundException {

		productsList = new ArrayList<>();
		retailersList = new ArrayList<>();
		Retailers retailers1 = new Retailers(1, "test3_Rakuten", "Tokyo");
		Retailers retailers2 = new Retailers(2, "test3_Seven Eleven", "Tokyo");
		retailersList.add(retailers1);
		retailersList.add(retailers2);
		Products products = new Products(1, "test3_Nokia", 1000, "Mobile", retailersList);

		Optional<Products> productsOptional = Optional.of(products);

		when(product_Repository.findById(products.getId())).thenReturn(productsOptional);

		products_ServiceImpl.deleteProducts(products.getId());
		verify(product_Repository, times(1)).deleteById(products.getId());
	}

	@Test
	@Order(4)
	public void test_productnotfoundException() throws ProductNotFoundException {
		Integer id = 10;
		ProductNotFoundException assertThrows2 = assertThrows(ProductNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {

				products_ServiceImpl.deleteProducts(id);

			}

		});

		assertEquals("products has not found with the id" + id, assertThrows2.getMessage());
	}

	@Test
	@Order(5)
	public void test_getRetailers() {
		
		
		productsList = new ArrayList<>();
		retailersList = new ArrayList<>();
		Retailers retailers1 = new Retailers(1, "test5_Rakuten", "Tokyo");
		Retailers retailers2 = new Retailers(2, "test5_Seven Eleven", "Tokyo");
		retailersList.add(retailers1);
		retailersList.add(retailers2);
		Products products = new Products(1, "test3_Nokia", 1000, "Mobile", retailersList);

		when(retailers_Repository.findByProductsId(products.getId())).thenReturn(retailersList);
		assertEquals(retailersList, products_ServiceImpl.getRetailers(products.getId()));
		
	}
	
	@Test
	@Order(6)
	public void test_searchProducts() {

		Retailers retailers1 = new Retailers(1, "test6_Rakuten", "Tokyo");
		Retailers retailers2 = new Retailers(2, "test6_Seven Eleven", "Tokyo");
		productsList = new ArrayList<>();
		retailersList = new ArrayList<>();
		retailersList.add(retailers1);
		retailersList.add(retailers2);

		Products products1 = new Products(1, "test6_Nokia", 1000, "Mobile", retailersList);
		Products products2 = new Products(2, "test6_Motorola", 1000, "Mobile", retailersList);
		productsList.add(products2);
		productsList.add(products1);

		when(product_Repository.findAll()).thenReturn(productsList);
		assertEquals(productsList, products_ServiceImpl.searchProducts(null, null));		
}
	
	@Test
	@Order(7)
	public void test_searchProducts_withKeyword() {

		Retailers retailers1 = new Retailers(1, "test6_Rakuten", "Tokyo");
		Retailers retailers2 = new Retailers(2, "test6_Seven Eleven", "Tokyo");
		productsList = new ArrayList<>();
		retailersList = new ArrayList<>();
		retailersList.add(retailers1);
		retailersList.add(retailers2);

		Products products1 = new Products(1, "test6_Nokia", 1000, "Mobile", retailersList);
		Products products2 = new Products(2, "test6_Motorola", 100, "cell", retailersList);
		productsList.add(products2);
		productsList.add(products1);
		List<Products> search = new ArrayList<>();
		search.add(products1);

		when(product_Repository.search("test6_Nokia", null)).thenReturn(search);
		assertEquals(search, products_ServiceImpl.searchProducts("test6_Nokia", null));

		
}

}