package com.ntt.poc.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntt.poc.entities.Products;

public interface Product_Repository extends JpaRepository<Products, Integer> {
	
	
	@Query("SELECT p FROM Products p WHERE p.productName LIKE %?1%"
            + " OR p.category LIKE %?1%"
            + " OR CONCAT(p.price, '') LIKE %?1%")
    public List<Products> search(String keyword, Pageable pageable);
	
}
