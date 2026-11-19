package com.smartdesk.service;

import org.springframework.stereotype.Service;

import com.smartdesk.entity.UserRole;
import com.smartdesk.repository.UserRoleRepository;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole createRole(String name) {

        if (userRoleRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException(
                    "Role already exists: " + name
            );
        }

        UserRole role = new UserRole(name);

        return userRoleRepository.save(role);
    }

    public UserRole getRoleByName(String name) {

        return userRoleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException(
                        "Role not found: " + name
                ));
    }
}