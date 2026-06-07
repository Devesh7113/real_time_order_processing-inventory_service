package com.real_time_order_processing.inventory.service;

import com.real_time_order_processing.inventory.dto.HelpdeskTicketSummaryDTO;
import com.real_time_order_processing.inventory.entity.HelpdeskTicket;
import com.real_time_order_processing.inventory.enums.TicketPriority;
import com.real_time_order_processing.inventory.repository.HelpdeskRepository;
import com.real_time_order_processing.inventory.request.TicketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HelpdeskService
{
    private final HelpdeskRepository helpdeskRepository;

    @Transactional
    public HelpdeskTicket createTicket(TicketRequest ticketInput)
    {
        LocalDateTime now = LocalDateTime.now();

        HelpdeskTicket ticket = new HelpdeskTicket();
        ticket.setUserId(ticketInput.getUserId());
        ticket.setQuery(ticketInput.getIssue());
        ticket.setStatus("OPEN");
        ticket.setPriority(TicketPriority.LOW);
        ticket.setResolved(false);
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);

        return helpdeskRepository.save(ticket);
    }

    public List<HelpdeskTicketSummaryDTO> getTicketsForUser(Long userId)
    {
        return helpdeskRepository.findByUserId(userId).stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public String updatePriority(Long ticketId, Long userId)
    {
        if (ticketId == null)
        {
            return "Error: ticket ID is required.";
        }
        if (userId == null)
        {
            return "Error: user ID is required.";
        }

        Optional<HelpdeskTicket> optional = helpdeskRepository.findById(ticketId);
        if (optional.isEmpty())
        {
            return "Ticket not found with id: " + ticketId + ".";
        }

        HelpdeskTicket helpdeskTicket = optional.get();
        if (!userId.equals(helpdeskTicket.getUserId()))
        {
            return "You can only escalate your own tickets.";
        }

        TicketPriority currentPriority = helpdeskTicket.getPriority();
        TicketPriority newPriority = switch (currentPriority)
        {
            case LOW -> TicketPriority.MEDIUM;
            case MEDIUM -> TicketPriority.HIGH;
            case HIGH -> null;
        };

        if (newPriority == null)
        {
            return "Ticket priority is already at maximum (HIGH).";
        }

        helpdeskTicket.setPriority(newPriority);
        helpdeskTicket.setUpdatedAt(LocalDateTime.now());
        helpdeskRepository.save(helpdeskTicket);

        return "Priority updated from " + currentPriority + " to " + newPriority + ".";
    }

    private HelpdeskTicketSummaryDTO toSummary(HelpdeskTicket ticket)
    {
        return HelpdeskTicketSummaryDTO.builder()
                .id(ticket.getId())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .resolved(ticket.getResolved())
                .createdAt(ticket.getCreatedAt())
                .issue(ticket.getQuery())
                .build();
    }
}
