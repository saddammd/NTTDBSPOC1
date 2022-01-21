package com.ntt.poc.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntt.poc.entities.AddProducts;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.ProductsResponse;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.entities.RetailersResponse;
import com.ntt.poc.exceptions.UserNotFoundException;
import com.ntt.poc.repository.Retailers_Repository;
import com.ntt.poc.service.Products_Service;
import com.ntt.poc.service.User_ServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/poc")
public class Products_Controller {

	@Autowired
	Products_Service product_service;

	@Autowired
	Retailers_Repository retailers_Repository;
	
	private Logger logger = Logger.getLogger(Products_Controller.class.getName());
	
	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN', 'USER')")
	@GetMapping("/products")
	public ResponseEntity<List<ProductsResponse>> listProducts(
			@RequestParam(value = "keyword",defaultValue="")String keyword,
			@RequestParam(value = "pageNoValue", defaultValue= "1") int pageNoValue,
			@RequestParam(value = "PageSize", defaultValue="100") int pageSize) {

		logger.info("role value passed in controller" +SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
		
		int pageNo = pageNoValue-1;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
 		List<Products> all_Products = product_service.searchProducts(keyword, pageable);
 		List<ProductsResponse> ProductsResponse = new ArrayList<>();
 		
 		for(Products prod: all_Products) { 
 			
 			ProductsResponse pr = new ProductsResponse();
 			BeanUtils.copyProperties(prod, pr);
 			ProductsResponse.add(pr);
 		}
 				
		return ResponseEntity.status(HttpStatus.OK).body(ProductsResponse);
	}


	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN')")
	@PostMapping("/products")
	public ResponseEntity<Products> saveProduct(@RequestBody AddProducts addproducts) {

		Products products = new Products();
		BeanUtils.copyProperties(addproducts, products);
		product_service.saveProducts(products);
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN')")
	@PutMapping("/products")
	public ResponseEntity<String> updateProduct(@RequestBody Products products) {

		product_service.saveProducts(products);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

    @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/products/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) throws UserNotFoundException {

		product_service.deleteProducts(id);

		return new ResponseEntity<String>(HttpStatus.OK);
	} 

	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN', 'USER')")
	@GetMapping("/retailers/{id}")
	public ResponseEntity<List<RetailersResponse>> getRetailers(@PathVariable Integer id) {
		List<Retailers> retailers = product_service.getRetailers(id);
		List<RetailersResponse> retailersResponse = new ArrayList<>();
		
		for(Retailers retail: retailers) {
 			
			RetailersResponse rr = new RetailersResponse();
 			BeanUtils.copyProperties(retail, rr);
 			retailersResponse.add(rr);
 		}
 		
		return ResponseEntity.status(HttpStatus.OK).body(retailersResponse);
	}

	
	
}
