package com.real_time_order_processing.inventory.request;

import lombok.Data;

@Data
public class TicketRequest
{
    String issue;
    Long userId;
}
