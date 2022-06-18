package com.bugTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bugTracker.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	@Query("SELECT r from Role r WHERE r.name = ?1")
	Role findByName(String role);

}
