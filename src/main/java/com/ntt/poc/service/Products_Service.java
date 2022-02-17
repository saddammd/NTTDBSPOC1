package com.ntt.poc.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.exceptions.ProductNotFoundException;
import com.ntt.poc.exceptions.UserNotFoundException;


public interface Products_Service {

	public List<Products> findAll();

	public Products saveProducts(Products products);

	public void deleteProducts(Integer id) throws ProductNotFoundException;

	public List<Retailers> getRetailers(Integer id);
	
	public List<Products> searchProducts(String keyword, Pageable pageable);
}
