package com.smartdesk.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartdesk.dto.UserRequest;
import com.smartdesk.dto.UserResponse;
import com.smartdesk.entity.User;
import com.smartdesk.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("")
	public List<UserResponse> getAllUsers(){
		return userService.getAllUsers().stream().map(user -> new UserResponse(user.getId(), user.getFullName(), user.getEmail())).toList();
	}
	
	@GetMapping("/{id}")
	public UserResponse getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);	
		return new UserResponse(user.getId(), user.getFullName(), user.getEmail());
	}
	
	@PostMapping("")
	public UserResponse registerUser(@Valid @RequestBody UserRequest requestUser) {
		
		User user = new User();
		user.setFullName(requestUser.getFullName());
		user.setEmail(requestUser.getEmail());
		 
		User savedUser = userService.registerUser(user);
		
		UserResponse responseUser = new UserResponse();
		responseUser.setFullName(savedUser.getFullName());
		responseUser.setEmail(savedUser.getEmail());
		responseUser.setId(savedUser.getId());
		
		//or using the parametrized constructor.. return new UserResponse(savedUser.getId(), savedUser.getFullName(), savedUser.getEmail());
		
		return responseUser;
	}
	
	@PutMapping("/{id}")
	public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest user) {
		
		User newUser = new User();
		newUser.setFullName(user.getFullName());
		newUser.setEmail(user.getEmail());
		
		User updateduser = userService.updateUser(id, newUser);
		return new UserResponse(updateduser.getId(), updateduser.getFullName(), updateduser.getEmail());
	}
	
	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return "User with id: "+id+" deleted successfully";
	}
}
