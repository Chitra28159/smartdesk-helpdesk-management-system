package com.smartdesk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartdesk.entity.User;
import com.smartdesk.exception.DuplicateResourceException;
import com.smartdesk.exception.ResourceNotFoundException;
import com.smartdesk.repository.UserRepository;


@Service
public class UserService {

	@Autowired
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Transactional
	public User registerUser(User user) {
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new DuplicateResourceException("Email already exists: " + user.getEmail());
		}
		
		return userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}
	
	@Transactional(readOnly = true)
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	@Transactional
	public User updateUser(Long id, User user) {
		User exisitngUser =  userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with this id: "+id));
		
		if(userRepository.existsByEmail(user.getEmail()) && !exisitngUser.getEmail().equalsIgnoreCase(user.getEmail())) {
			throw new DuplicateResourceException("Email already exists: " + user.getEmail());
			
		}
		
		exisitngUser.setFullName(user.getFullName());
		exisitngUser.setEmail(user.getEmail());
		return userRepository.save(exisitngUser);
	}
	@Transactional
	public void deleteUser(Long id) {
		
		if(!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with this id: "+id);
		}
		userRepository.deleteById(id);
	}
	
}
