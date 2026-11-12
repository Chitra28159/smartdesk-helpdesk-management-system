package com.smartdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartdesk.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
