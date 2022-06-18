package com.bugTracker.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="projects")
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable= false,length = 45)
	private String projectName;
	
	@Column(nullable= false, length = 64)
	private String description;
		
	@Column(nullable= false, length = 64)
	private String createdAt;
	
	@JsonManagedReference 
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
	private Set<User> contributors = new HashSet<>();// the reason to use the set to store the contributors is because set does not allow to store the duplicate elements
	
	@JsonManagedReference 
	@OneToMany(mappedBy="project",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Ticket> tickets = new HashSet<>(); // store a list of the tickets for the selected project 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
	public Set<User> getContributors() {
		return contributors;
	}

	public void setContributors(Set<User> contributors) {
		this.contributors = contributors;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public void addContributors(User contributor) {
		this.contributors.add(contributor);
	}
	
	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	public void addTickets(Ticket ticket) {
		this.tickets.add(ticket);
	}
}
