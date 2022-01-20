package com.ntt.poc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.ntt.poc.entities.AddRetailers;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.exceptions.UserNotFoundException;
import com.ntt.poc.repository.Product_Repository;
import com.ntt.poc.repository.Retailers_Repository;

@Service
public class Retailers_ServiceImpl implements Retailers_Service{

	@Autowired
	Retailers_Repository retailers_Repository;
	
	@Autowired
	Product_Repository product_Repository;

	
	@Override
	public List<Retailers> findAll(Integer productid) {
		return retailers_Repository.findByProductsId(productid);
	}

	@Override
	public Retailers saveRetailer(AddRetailers addretailers) {
		
		Products products = product_Repository.getById(addretailers.getProducts());
		Retailers retailers = new Retailers();
		BeanUtils.copyProperties(addretailers, retailers);
		retailers.setProducts(products);
		retailers.getProducts().setId(addretailers.getProducts());
		return retailers_Repository.save(retailers);
	}

	@Override
	public void deleteRetailer(Integer id) throws UserNotFoundException {
		retailers_Repository.deleteById(id);
		
	}

	@Override
	public Optional<Retailers> getRetailer(Integer id) {
		return retailers_Repository.findById(id);
	}

	@Override
	public List<Retailers> searchRetailers(Integer id, String keyword) {
		
		if(keyword.equals("")) {
			
			return findAll(id);
		}
				return retailers_Repository.search(id, keyword);
	}

	@Override
	public Retailers getRetailer(Integer productid, Integer retailerid) {
		
		return retailers_Repository.findByProductsIdAndRetailerId(productid, retailerid);		
	}

	}
