
package com.smartdesk.mapper;

import org.springframework.stereotype.Component;

import com.smartdesk.dto.TicketRequest;
import com.smartdesk.dto.TicketResponse;
import com.smartdesk.entity.Ticket;
import com.smartdesk.entity.User;

@Component
public class TicketMapper {

    public Ticket toEntity(TicketRequest request, User createdBy) {

        Ticket ticket = new Ticket();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCreatedBy(createdBy);

        return ticket;
    }

    public TicketResponse toResponse(Ticket ticket) {

        User createdBy = ticket.getCreatedBy();
        User assignedTo = ticket.getAssignedTo();

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                createdBy.getId(),
                createdBy.getFullName(),
                assignedTo != null ? assignedTo.getId() : null,
                assignedTo != null ? assignedTo.getFullName() : null
        );
    }
}
