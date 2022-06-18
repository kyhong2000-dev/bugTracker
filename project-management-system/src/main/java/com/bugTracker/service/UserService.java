package com.bugTracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bugTracker.entity.Project;
import com.bugTracker.entity.Role;
import com.bugTracker.entity.User;
import com.bugTracker.repository.ProjectRepository;
import com.bugTracker.repository.RoleRepository;
import com.bugTracker.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private ProjectRepository projectRepo;
	
	//assign default role when register the user 
	public void saveUserwithDefaultRole(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //spring built-in api to hash the password
	    String encodedPassword = passwordEncoder.encode(user.getPassword()); //retrieve the user password and then encrypt it
	    user.setPassword(encodedPassword); //set the new-updated encrypted password
	    
	    /**
	     * Application will have only one administration user account
	     * Every registered user will be assigned with the "User" role. 
	     * "User"role will be allowed to access everything related with the project / issue tracking
	     * Administration related task like manage the user will be forbidden for the normal user role 
	     * */	    
	    Role roleUser = roleRepo.findByName("User"); //retrieve the <<User>> role from repo
	    user.addRole(roleUser); //assign default 'User' role to every new registered user
	    user.setProject(null);
	    
		userRepo.save(user); //use repo save it to update the data in the db
	}
	
	//find username with the principal emails
	public User findUsernameByPrincipalEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	public List<User> findUserList(){
		return userRepo
				.findAll() //fetch all the users record data 
				.stream()
				.filter(i->i.hasRole("User"))
				.toList();
	}
	
	public User findUserByUsername(String username){
		return userRepo.findByUsername(username);
	}
	
	public Optional<User> findById(Long id) {
		return userRepo.findById(id);
	}
	
	public User findUserByUserId(Long id) {
		return userRepo.findUserByUserID(id);
	}
	
	
}
