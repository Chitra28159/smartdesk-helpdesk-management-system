package com.smartdesk.exception;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> ResourceNotFoundExcptn(ResourceNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",ex.getMessage()));
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Map<String, String>> DuplicateResourceExcptn(DuplicateResourceException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",ex.getMessage()));
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, List<String>>> MethodArgNotValidExcptn(MethodArgumentNotValidException ex){
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors:", errors));
		
		// if streams aren't used, this is also valid.. just for my ref..
		// for(FieldError error: ex.getBindingResult().getFieldErrors()) {
		// errors.add(error.getDefaultMessage());
		//}
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
	        IllegalArgumentException ex) {

	    Map<String, String> error = Map.of(
	            "error", ex.getMessage()
	    );

	    return ResponseEntity.badRequest().body(error);
	}
	
}
