package com.real_time_order_processing.inventory.dto;

import lombok.Data;

@Data
public class InventoryProcessRequestData
{
    private Long productId;
    private Integer quantity;
}
