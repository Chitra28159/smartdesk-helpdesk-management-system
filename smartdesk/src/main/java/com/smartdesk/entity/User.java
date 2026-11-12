package com.smartdesk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@NotBlank(message = "Full name is required") commenting caz the validation is already done in the DTO layer
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
//	@NotBlank(message = "Email is required") commenting caz the validation is already done in the DTO layer
//	@Email(message = "Email should be valid") commenting caz the validation is already done in the DTO layer
	@Column(nullable = false, unique = true)
	private String email;
	
	public User() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
//	public User(Long id, String fullName, String email) {
//		super(); // Call to the superclass constructor means that this constructor is calling the constructor of the superclass (in this case, Object) before executing its own code. This is done to ensure that any initialization in the superclass is performed before the subclass's constructor logic is executed.
//		this.id = id;
//		this.fullName = fullName;
//		this.email = email;
//	}
}
