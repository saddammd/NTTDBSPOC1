package com.ntt.poc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Exception_Handlers {

	@ExceptionHandler
	public ResponseEntity<UserNotFoundException> handleException(UserNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
	}
	
	@ExceptionHandler
	public ResponseEntity<DuplicateRegistration> handleException(DuplicateRegistration ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
	}
	
	@ExceptionHandler
	public ResponseEntity<ProductNotFoundException> handleException(ProductNotFoundException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
	}
}
