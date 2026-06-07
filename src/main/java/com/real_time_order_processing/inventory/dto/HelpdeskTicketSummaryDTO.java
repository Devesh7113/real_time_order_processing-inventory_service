package com.real_time_order_processing.inventory.dto;

import com.real_time_order_processing.inventory.enums.TicketPriority;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HelpdeskTicketSummaryDTO
{
    private Long id;
    private String status;
    private TicketPriority priority;
    private Boolean resolved;
    private LocalDateTime createdAt;
    private String issue;
}
