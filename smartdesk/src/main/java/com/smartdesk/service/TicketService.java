
package com.smartdesk.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartdesk.dto.TicketRequest;
import com.smartdesk.dto.TicketResponse;
import com.smartdesk.entity.Ticket;
import com.smartdesk.entity.User;
import com.smartdesk.enums.TicketPriority;
import com.smartdesk.enums.TicketStatus;
import com.smartdesk.exception.ResourceNotFoundException;
import com.smartdesk.mapper.TicketMapper;
import com.smartdesk.repository.TicketRepository;
import com.smartdesk.repository.UserRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            TicketMapper ticketMapper) {

        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public TicketResponse createTicket(TicketRequest request) {

        User createdBy = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getCreatedById()));

        Ticket ticket = ticketMapper.toEntity(request, createdBy);

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(savedTicket);
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket not found with id: " + id));

        return ticketMapper.toResponse(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {

        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }
    
    @Transactional
    public TicketResponse assignTicket(Long ticketId, Long userId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket not found with id: " + ticketId));

        User assignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        ticket.setAssignedTo(assignedUser);

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(updatedTicket);
    }
    
    @Transactional
    public TicketResponse updateTicketStatus(Long ticketId, TicketStatus status) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket not found with id: " + ticketId));

        ticket.setStatus(status);

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(updatedTicket);
    }
    
    @Transactional
    public TicketResponse updateTicketPriority(Long ticketId, TicketPriority priority) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket not found with id: " + ticketId));

        ticket.setPriority(priority);

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(updatedTicket);
    }
    
    @Transactional(readOnly = true)
    public Page<TicketResponse> getFilteredTickets(
            TicketStatus status,
            TicketPriority priority,
            int page,
            int size,
            String sortBy,
            String direction) {
    	
    	if (page < 0) {
    	    throw new IllegalArgumentException("Page number cannot be negative.");
    	}

    	if (size <= 0 || size > 100) {
    	    throw new IllegalArgumentException(
    	            "Page size must be between 1 and 100.");
    	}
    	
    	Set<String> allowedSortFields = Set.of(
    	        "id", "title", "createdAt", "updatedAt", "status", "priority"
    	);

    	if (!allowedSortFields.contains(sortBy)) {
    	    throw new IllegalArgumentException(
    	            "Invalid sort field: " + sortBy);
    	}

    	if (!direction.equalsIgnoreCase("asc")
    	        && !direction.equalsIgnoreCase("desc")) {
    	    throw new IllegalArgumentException(
    	            "Sort direction must be either 'asc' or 'desc'.");
    	}

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Ticket> tickets;

        if (status != null && priority != null) {
            tickets = ticketRepository.findByStatusAndPriority(
                    status, priority, pageable);

        } else if (status != null) {
            tickets = ticketRepository.findByStatus(status, pageable);

        } else if (priority != null) {
            tickets = ticketRepository.findByPriority(priority, pageable);

        } else {
            tickets = ticketRepository.findAll(pageable);
        }

        return tickets.map(ticketMapper::toResponse);
    }
}
