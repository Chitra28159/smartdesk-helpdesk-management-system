
package com.smartdesk.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.smartdesk.dto.TicketRequest;
import com.smartdesk.dto.TicketResponse;
import com.smartdesk.enums.TicketPriority;
import com.smartdesk.enums.TicketStatus;
import com.smartdesk.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("")
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody TicketRequest request) {

        TicketResponse response = ticketService.createTicket(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("")
    public Page<TicketResponse> getFilteredTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ticketService.getFilteredTickets(
                status, priority, page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @PatchMapping("/{ticketId}/assign/{userId}")
    public TicketResponse assignTicket(
            @PathVariable Long ticketId,
            @PathVariable Long userId) {

        return ticketService.assignTicket(ticketId, userId);
    }

    @PatchMapping("/{ticketId}/status")
    public TicketResponse updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam TicketStatus status) {

        return ticketService.updateTicketStatus(ticketId, status);
    }

    @PatchMapping("/{ticketId}/priority")
    public TicketResponse updateTicketPriority(
            @PathVariable Long ticketId,
            @RequestParam TicketPriority priority) {

        return ticketService.updateTicketPriority(ticketId, priority);
    }
}
