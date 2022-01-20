package com.ntt.poc.entities;

public class RetailersResponse {
	
	private Integer retailerId;
	private String retailerName;
	private String location;
	
	public RetailersResponse() {
		super();
		// TODO Auto-generated constructor stub
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

	@Override
	public String toString() {
		return "RetailersResponse [retailerId=" + retailerId + ", retailerName=" + retailerName + ", location="
				+ location + "]";
	}
	
	

}
