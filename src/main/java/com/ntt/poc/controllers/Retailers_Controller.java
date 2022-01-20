package com.ntt.poc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntt.poc.entities.AddRetailers;
import com.ntt.poc.entities.Products;
import com.ntt.poc.entities.Retailers;
import com.ntt.poc.entities.RetailersResponse;
import com.ntt.poc.exceptions.UserNotFoundException;
import com.ntt.poc.repository.Product_Repository;
import com.ntt.poc.repository.Retailers_Repository;
import com.ntt.poc.service.Retailers_Service;

@CrossOrigin
@RestController
@RequestMapping("/poc")
public class Retailers_Controller {

	@Autowired
	Retailers_Service retailers_Service;

	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN', 'USER')")
	@GetMapping("/retailers")
	public ResponseEntity<List<RetailersResponse>> listRetailers(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "keyword", defaultValue = "") String keyword) {

		List<Retailers> all_Retailers = retailers_Service.searchRetailers(id, keyword);
		System.out.println(all_Retailers);
		List<RetailersResponse> retailersResponse = new ArrayList<>();
		
		for(Retailers r:all_Retailers) {
			RetailersResponse target = new RetailersResponse();
			BeanUtils.copyProperties(r, target);
			retailersResponse.add(target);
			System.out.println(retailersResponse);
		}
		return new ResponseEntity(retailersResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN', 'USER')")
	@GetMapping("/retailers/{productid}/")
	public ResponseEntity<RetailersResponse> getRetailer(@PathVariable Integer productid,
			@RequestParam(value = "retailerid") Integer retailerid) {

		Retailers retailer = retailers_Service.getRetailer(productid, retailerid);
		RetailersResponse rs = new RetailersResponse();
		BeanUtils.copyProperties(retailer, rs);
		return new ResponseEntity(rs, HttpStatus.OK);
	}
	
	
	
	
	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN')")
	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/retailers", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity saveRetailer(@RequestBody AddRetailers addretailers) {
		retailers_Service.saveRetailer(addretailers);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('SUPERUSER', 'ADMIN')")
	@SuppressWarnings("rawtypes")
	@PutMapping(path = "/retailers", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity updateRetailer(@RequestBody AddRetailers addretailers) {
		retailers_Service.saveRetailer(addretailers);
		return new ResponseEntity(HttpStatus.OK);
	}

    @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/retailers/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) throws UserNotFoundException {

		retailers_Service.deleteRetailer(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	} 


}