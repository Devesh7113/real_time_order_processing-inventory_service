package com.real_time_order_processing.inventory.dto;

import lombok.Data;

@Data
public class InventoryProcessResponseData
{
    private Long productId;
    private Boolean available;
}
