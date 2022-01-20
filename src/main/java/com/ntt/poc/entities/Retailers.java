package com.ntt.poc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Retailers")
public class Retailers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer retailerId;
	private String retailerName;
	private String location;
	
	@ManyToOne
	@JoinColumn(name = "productsId")
	private Products products;

	public Retailers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Retailers(Integer retailerId, String retailerName, String location) {
		super();
		this.retailerId = retailerId;
		this.retailerName = retailerName;
		this.location = location;
		
	}

	public Integer getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Retailers [retailerId=" + retailerId + ", retailerName=" + retailerName + ", location=" + location
				+ ", products=" + products + "]";
	}

	
	
}
