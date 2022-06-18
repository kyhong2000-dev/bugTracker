package com.bugTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bugTracker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);	
	
	@Query("SELECT u FROM User u WHERE u.username=?1")
	User findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.id=?1")
	User findUserByUserID(Long userId);
}
