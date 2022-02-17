package com.ntt.poc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.exceptions.ProductNotFoundException;
import com.ntt.poc.exceptions.UserNotFoundException;
import com.ntt.poc.repository.Product_Repository;
import com.ntt.poc.repository.Retailers_Repository;

@Service
public class Products_ServiceImpl implements Products_Service{

	@Autowired
	Product_Repository product_Repository;
	
	@Autowired
	Retailers_Repository retailers_Repository;
	
	@Override
	public List<Products> findAll() {
		return product_Repository.findAll();
	}


	@Override
	public Products saveProducts(Products products) {
		
		return product_Repository.save(products);
	}


	@Override
	public void deleteProducts(Integer id) throws ProductNotFoundException {
		Optional<Products> Products =  product_Repository.findById(id);
		
		if(Products.isPresent()) {
		product_Repository.deleteById(id);
		}
		
		else throw new ProductNotFoundException("products has not found with the id" +id);
		
	}


	@Override
	public List<Retailers> getRetailers(Integer id) {
						
		return retailers_Repository.findByProductsId(id);
	}


	@Override
	public List<Products> searchProducts(String keyword, Pageable pageable) {
		
		if((keyword!=null) && (keyword!="")) {
			return product_Repository.search(keyword, pageable);
		}
		
		return findAll();
	}

}
