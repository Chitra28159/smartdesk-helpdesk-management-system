package com.smartdesk.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {
	
	@NotBlank(message = "Full Name is required")
	private String fullName;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	private String email;
	
	public UserRequest() {
		
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
