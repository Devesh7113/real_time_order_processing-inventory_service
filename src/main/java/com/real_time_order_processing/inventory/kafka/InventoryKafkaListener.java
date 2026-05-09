package com.real_time_order_processing.inventory.kafka;

import com.real_time_order_processing.inventory.dto.InventoryProcessRequest;
import com.real_time_order_processing.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InventoryKafkaListener
{
    private final InventoryService inventoryService;

    @KafkaListener(
            topics ="process-inventory",
            containerFactory = "inventoryKafkaListenerContainerFactory"
    )

    public void processInventory(InventoryProcessRequest request)
    {
        inventoryService.processInventory(request);
    }
}
