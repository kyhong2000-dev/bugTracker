package com.bugTracker.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bugTracker.entity.Project;
import com.bugTracker.entity.Ticket;
import com.bugTracker.entity.User;
import com.bugTracker.repository.ProjectRepository;
import com.bugTracker.repository.TicketRepository;

@Service
public class TicketService {
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private TicketRepository ticketRepo;
	
	public void saveTicketDetails(Ticket ticket,Long projectId, User developer) {
		Project foundProject = projectRepo.findProjectById(projectId);//find the project with the id retrieved in the query params
		ticket.assignDevsForSelectedTickets(developer); //assign a list of developers for the tickets
		ticketRepo.save(ticket); //save the ticket details into database 
		foundProject.addTickets(ticket);// for-loop add all the tickets for the selected project 
		projectRepo.save(foundProject);//save the project details into database 
	}
	
	public List<Ticket> findAllProjectTickets() {
		return ticketRepo.findAll();
	}
	
	public Page<Ticket> findPaginated(int pageNo, int pageSize){
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize); //in page request,it start with index 0, so need to minus 1 to get the actual page number
		return this.ticketRepo.findAll(pageable);
	}
	
	public Page<Ticket> filterPageByAuthor(int pageNo, int pageSize,String authorName){
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return this.ticketRepo.findByAuthor(authorName, pageable);
	}
	
	public Optional<Ticket> getOneTicket(Long id){
		return ticketRepo.findById(id);
	}
	
	public Integer findTicketCountByTicketStatus(String ticketStatus) {
		return ticketRepo.findTicketCountByTicketStatus(ticketStatus);
	}
	
	public Integer findTicketCountByTicketPriority(String ticketPriority) {
		return ticketRepo.findTicketCountByTicketPriority(ticketPriority);
	}
	
	public Integer findTicketCountByTicketType(String ticketType) {
		return ticketRepo.findTicketCountByTicketType(ticketType);
	}
	
	public Integer findTicketTypeCountByProjectId(Long projectId,String ticketType) {
		Project project = projectRepo.findProjectById(projectId);
		Set<Ticket> ticketCollectionSet = project.getTickets();
		
		//convert set <ticket> to list <ticket>
	    long count = ticketCollectionSet
				.stream()
				.filter(i-> i.getTicketType().equalsIgnoreCase(ticketType))
				.count();
	    Integer ticketTypeCount = (int) count;
	    
	    return ticketTypeCount;
	}
	
	public Integer findTicketStatusCountByProjectId(Long projectId, String ticketStatus) {
		Project project = projectRepo.findProjectById(projectId);
		Set<Ticket> ticketCollectionSet = project.getTickets();
		
		//convert set <ticket> to list <ticket>
	    long count = ticketCollectionSet
				.stream()
				.filter(i-> i.getStatus().equalsIgnoreCase(ticketStatus))
				.count();
	    Integer ticketStatusCount = (int) count;
		
	    return ticketStatusCount;
	}
	
	public Integer findTicketPriorityCountByProjectId(Long projectId, String ticketPriority) {
		Project project = projectRepo.findProjectById(projectId);
		Set<Ticket> ticketCollectionSet = project.getTickets();
		
		//convert set <ticket> to list <ticket>
	    long count = ticketCollectionSet
				.stream()
				.filter(i-> i.getPriority().equalsIgnoreCase(ticketPriority))
				.count();
	    Integer ticketPriorityCount = (int) count;
		
	    return ticketPriorityCount;
	}
	
	
	public Integer findTotalTicketCount() {
		return ticketRepo.findTotalTicket();
	}
	
	public void deleteSelectedTicket(Long id) {
		Ticket foundTicket = ticketRepo.findTicketById(id);
		ticketRepo.delete(foundTicket);
	}
	
	public void updateTicketEditDetails(Long id,String ticketTitle,String description,String author,String status,String priority,String ticketType) {
		Ticket ticket = ticketRepo.findTicketById(id);
		ticket.setAuthor(author);
		ticket.setDescription(description);
		ticket.setPriority(priority);
		ticket.setStatus(status);
		ticket.setTicketTitle(ticketTitle);
		ticket.setTicketType(ticketType);
		ticketRepo.save(ticket);
	}
}
