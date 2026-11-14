package com.smartdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartdesk.entity.Ticket;
import com.smartdesk.enums.TicketPriority;
import com.smartdesk.enums.TicketStatus;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByStatusAndPriority(
            TicketStatus status,
            TicketPriority priority,
            Pageable pageable
    );
    
    Page<Ticket> findByStatus(
            TicketStatus status, Pageable pageable);

    Page<Ticket> findByPriority(
            TicketPriority priority, Pageable pageable);
}