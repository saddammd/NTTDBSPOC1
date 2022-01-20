package com.ntt.poc.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Products {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Integer id;
	private String productName;
	private Integer price;
	private String category;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "products" )
	//@JoinColumn(name="productsId", referencedColumnName = "id")
	private List<Retailers> retailers;
	
	
	public Products() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Products(String productName, Integer price, String category, List<Retailers> retailers) {
		super();
		this.productName = productName;
		this.price = price;
		this.category = category;
		this.retailers = retailers;
	}



	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	public List<Retailers> getRetailers() {
		return retailers;
	}

	public void setRetailers(List<Retailers> retailers) {
		this.retailers = retailers;
	}



	@Override
	public String toString() {
		return "Products [id=" + id + ", productName=" + productName + ", price=" + price + ", category=" + category
				+ "]";
	}
	
	
	

}
