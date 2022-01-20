package com.ntt.poc.entities;

public class ProductsResponse {

	private Integer id;
	private String productName;
	private Integer price;
	private String category;
	
	
	public ProductsResponse() {
		super();
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


	@Override
	public String toString() {
		return "ProductsResponse [id=" + id + ", productName=" + productName + ", price=" + price + ", category="
				+ category + ", getId()=" + getId() + ", getProductName()=" + getProductName() + ", getPrice()="
				+ getPrice() + ", getCategory()=" + getCategory() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	
	
	
}
