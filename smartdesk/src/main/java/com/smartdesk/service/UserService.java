
package com.smartdesk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartdesk.dto.UserRequest;
import com.smartdesk.dto.UserResponse;
import com.smartdesk.entity.User;
import com.smartdesk.exception.DuplicateResourceException;
import com.smartdesk.exception.ResourceNotFoundException;
import com.smartdesk.mapper.UserMapper;
import com.smartdesk.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponse registerUser(UserRequest request) {

        User user = userMapper.toEntity(request);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException(
                "Email already exists: " + user.getEmail()
            );
        }

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "User not found with id: " + id
                    )
                );

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "User not found with id: " + id
                    )
                );

        if (userRepository.existsByEmail(request.getEmail())
                && !existingUser.getEmail()
                    .equalsIgnoreCase(request.getEmail())) {

            throw new DuplicateResourceException(
                "Email already exists: " + request.getEmail()
            );
        }

        User updatedDetails = userMapper.toEntity(request);

        existingUser.setFullName(updatedDetails.getFullName());
        existingUser.setEmail(updatedDetails.getEmail());

        User savedUser = userRepository.save(existingUser);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "User not found with id: " + id
            );
        }

        userRepository.deleteById(id);
    }
}
