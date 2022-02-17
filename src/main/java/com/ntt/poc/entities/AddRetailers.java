package com.ntt.poc.entities;

public class AddRetailers {

		private Integer retailerId;
		private String retailerName;
		private String location;
		private Integer products;
		
		
		
		
		public AddRetailers(Integer retailerId, String retailerName, String location, Integer products) {
			super();
			this.retailerId = retailerId;
			this.retailerName = retailerName;
			this.location = location;
			this.products = products;
		}
		
		


		public AddRetailers(String retailerName, String location, Integer products) {
			super();
			this.retailerName = retailerName;
			this.location = location;
			this.products = products;
		}




		public AddRetailers() {
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


		public Integer getProducts() {
			return products;
		}


		public void setProducts(Integer products) {
			this.products = products;
		}


		@Override
		public String toString() {
			return "AddRetailers [retailerId=" + retailerId + ", retailerName=" + retailerName + ", location="
					+ location + ", products=" + products + "]";
		}
		
		
		

		

}
