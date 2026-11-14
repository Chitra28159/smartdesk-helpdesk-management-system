
package com.smartdesk.dto;

import java.time.LocalDateTime;

import com.smartdesk.enums.TicketPriority;
import com.smartdesk.enums.TicketStatus;

public class TicketResponse {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long createdById;
    private String createdByName;

    private Long assignedToId;
    private String assignedToName;

    public TicketResponse() {
    }

    public TicketResponse(
            Long id,
            String title,
            String description,
            TicketStatus status,
            TicketPriority priority,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long createdById,
            String createdByName,
            Long assignedToId,
            String assignedToName) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdById = createdById;
        this.createdByName = createdByName;
        this.assignedToId = assignedToId;
        this.assignedToName = assignedToName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }
}
