package com.bugTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bugTracker.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	@Query("SELECT p FROM Project p WHERE p.id =?1")
	Project findProjectById(Long projectId);
}
