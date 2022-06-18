package com.bugTracker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.bugTracker.entity.Role;
import com.bugTracker.entity.User;
import com.bugTracker.repository.RoleRepository;
import com.bugTracker.repository.UserRepository;
import com.bugTracker.service.UserService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Test
	public void testAddRoleToNewUser() {
		User user = new User();
		user.setEmail("admin@gmail.com");
		user.setUsername("admin");
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //spring built-in api to hash the password
	    String encodedPassword = passwordEncoder.encode("admin"); //retrieve the user password and then encrypt it
	    user.setPassword(encodedPassword); //set the new-updated encrypted password
	    
	    Role roleAdmin = roleRepo.findByName("Admin"); //retrieve the <<User>> role from repo
	    user.addRole(roleAdmin); //assign default 'User' role to every new registered user
	    user.setProject(null);
	    
		userRepo.save(user); //use repo save it to update the data in the db
		
		assertThat(user.getRoles().size()>1);
	}
}
