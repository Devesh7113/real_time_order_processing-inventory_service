package com.real_time_order_processing.inventory.tools;

import com.real_time_order_processing.inventory.dto.HelpdeskTicketSummaryDTO;
import com.real_time_order_processing.inventory.entity.HelpdeskTicket;
import com.real_time_order_processing.inventory.request.TicketRequest;
import com.real_time_order_processing.inventory.service.HelpdeskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpDeskTools
{
    private final HelpdeskService helpdeskService;

    @Tool(name = "createTicket", description = "Create a new help desk support ticket for a logged-in customer")
    String createTicket(
            @ToolParam(description = "Logged-in customer user ID from chat context") Long userId,
            @ToolParam(description = "Brief issue description summarizing the customer's problem") String issue)
    {
        if (userId == null)
        {
            return "Error: userId is required to create a ticket.";
        }
        if (issue == null || issue.isBlank())
        {
            return "Error: issue description is required to create a ticket.";
        }

        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setUserId(userId);
        ticketRequest.setIssue(issue.trim());

        log.info("Creating support ticket with details: {}", ticketRequest);
        HelpdeskTicket savedTicket = helpdeskService.createTicket(ticketRequest);
        log.info("Ticket created successfully. Ticket ID: {}, User ID: {}", savedTicket.getId(), savedTicket.getUserId());
        return "Ticket #" + savedTicket.getId() + " created successfully for user " + savedTicket.getUserId()
                + " with status " + savedTicket.getStatus();
    }

    @Tool(name = "getTicketStatus", description = "Fetch help desk tickets for a user by user ID")
    List<HelpdeskTicketSummaryDTO> getTicketStatus(
            @ToolParam(description = "User ID to fetch help desk tickets for") Long userId)
    {
        if (userId == null)
        {
            log.warn("getTicketStatus called without userId");
            return List.of();
        }

        log.info("Fetching tickets for user ID: {}", userId);
        List<HelpdeskTicketSummaryDTO> tickets = helpdeskService.getTicketsForUser(userId);
        log.info("Found {} tickets for user ID: {}", tickets.size(), userId);
        return tickets;
    }

    @Tool(name = "updateTicketPriority", description = "Escalate an existing help desk ticket priority by one level (LOW to MEDIUM, MEDIUM to HIGH). Requires ticket ID from getTicketStatus and the owning user ID. Cannot escalate tickets already at HIGH priority.")
    String updateTicketPriority(
            @ToolParam(description = "Unique ticket ID to escalate priority for") Long ticketId,
            @ToolParam(description = "User ID that owns the ticket") Long userId)
    {
        log.info("Updating priority for ticket ID: {} and user ID: {}", ticketId, userId);
        String result = helpdeskService.updatePriority(ticketId, userId);
        log.info("Priority update result for ticket ID {}: {}", ticketId, result);
        return result;
    }
}
