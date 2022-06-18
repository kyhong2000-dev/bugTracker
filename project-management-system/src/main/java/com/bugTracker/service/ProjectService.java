package com.bugTracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugTracker.entity.Project;
import com.bugTracker.entity.User;
import com.bugTracker.repository.ProjectRepository;
import com.bugTracker.repository.UserRepository;

@Service
public class ProjectService {
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public void saveProjectDetails(Project project, User contributor) {
		project.addContributors(contributor);
		projectRepo.save(project);  //save project details
		contributor.setProject(project);
		userRepo.save(contributor); //save user details 
	}
	
	public List<Project> findAllCreatedProject() {
		return projectRepo.findAll();
	}
	
	public Project findProjectById(Long projectId) {
		return projectRepo.findProjectById(projectId);
	}
	
	public void update(Project project) {
		projectRepo.save(project);
	}
	
	public void updateProjectEditDetails(Long id,String projectName, String description) {
		Project project = projectRepo.findProjectById(id);
		project.setProjectName(projectName);
		project.setDescription(description);
		projectRepo.save(project);
	}
	
	public void deleteSelectedProject(Long id) {
		Project foundProject = projectRepo.findProjectById(id);
		for(User contributor:foundProject.getContributors()) {
			contributor.setProject(null);
		}
		projectRepo.delete(foundProject);
	}
	
	//add available contributors that does not have team to the existing project team 
	public void addTeamMembers(Long projectId,User contributor) {
		Project foundProject = projectRepo.findProjectById(projectId);
		foundProject.addContributors(contributor);
		projectRepo.save(foundProject);
		contributor.setProject(foundProject);
		userRepo.save(contributor);
	}
	
	//delete the team member from the project teams
	public void deleteTeamMembersFromProjectTeam(User contributor) {
		//Project foundProject = projectRepo.findProjectById(projectId);
		contributor.setProject(null);
		userRepo.save(contributor);
	}
}
