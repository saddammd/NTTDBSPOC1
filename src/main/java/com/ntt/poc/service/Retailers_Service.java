package com.ntt.poc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ntt.poc.entities.AddRetailers;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.exceptions.UserNotFoundException;


public interface Retailers_Service {

	public List<Retailers> findAll(Integer productid);

	public Retailers saveRetailer(AddRetailers addretailers);

	public void deleteRetailer(Integer id) throws UserNotFoundException;

	public Optional<Retailers> getRetailer(Integer id);
	
	public List<Retailers> searchRetailers(Integer id, String keyword);
	
	public Retailers getRetailer(Integer productid, Integer retailerid);
}
