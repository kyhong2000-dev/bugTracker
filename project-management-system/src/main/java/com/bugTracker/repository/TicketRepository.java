package com.bugTracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bugTracker.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	@Query("SELECT COUNT(t) from Ticket t WHERE t.status =?1")
	Integer findTicketCountByTicketStatus(String status);
	
	@Query("SELECT COUNT(t) from Ticket t WHERE t.priority=?1")
	Integer findTicketCountByTicketPriority(String priority);
	
	@Query("SELECT COUNT(t) from Ticket t WHERE t.ticketType=?1")
	Integer findTicketCountByTicketType(String ticketType);
	
	@Query("SELECT COUNT(t) from Ticket t")
	Integer findTotalTicket();
	
	@Query("SELECT t FROM Ticket t WHERE t.id =?1")
	Ticket findTicketById(Long ticketId);
	
	Page<Ticket>findByAuthor(String authorName,Pageable pageable);
}
