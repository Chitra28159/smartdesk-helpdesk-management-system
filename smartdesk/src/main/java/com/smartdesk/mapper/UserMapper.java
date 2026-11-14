package com.smartdesk.mapper;

import org.springframework.stereotype.Component;

import com.smartdesk.dto.UserRequest;
import com.smartdesk.dto.UserResponse;
import com.smartdesk.entity.User;

@Component 
public class UserMapper {
	
	public User toEntity(UserRequest userRequest) {
		User user = new User();
		user.setFullName(userRequest.getFullName());
		user.setEmail(userRequest.getEmail());
		return user;
	}
	
	public UserResponse toResponse(User user) {
		UserResponse userResponse = new UserResponse(user.getId(), user.getFullName(), user.getEmail());
		return userResponse;
	}

}
