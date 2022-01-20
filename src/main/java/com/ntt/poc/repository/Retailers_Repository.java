package com.ntt.poc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;

public interface Retailers_Repository extends JpaRepository<Retailers, Integer> {

	@Query("SELECT r FROM Retailers r WHERE r.products.id=?1 ")
	public List<Retailers> findByProductsId(Integer id);

	@Query("SELECT r FROM Retailers r WHERE r.products.id=?1"
			+ " and r.retailerName LIKE %?2%"
			+ "OR "
			+ "r.products.id=?1 and r.location LIKE %?2%")
	public List<Retailers> search(Integer id, String keyword); 
	
	public Retailers findByProductsIdAndRetailerId(Integer productid, Integer retailersid);

}
