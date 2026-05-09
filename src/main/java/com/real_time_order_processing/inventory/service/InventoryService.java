package com.real_time_order_processing.inventory.service;

import com.real_time_order_processing.inventory.dto.InventoryProcessRequest;
import com.real_time_order_processing.inventory.dto.InventoryProcessRequestData;
import com.real_time_order_processing.inventory.dto.InventoryProcessResponse;
import com.real_time_order_processing.inventory.dto.InventoryProcessResponseData;
import com.real_time_order_processing.inventory.entity.Product;
import com.real_time_order_processing.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService
{
    private final ProductRepository productRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void processInventory(InventoryProcessRequest request)
    {
        List<Long> productIds = request.getProductList().stream()
                .map(InventoryProcessRequestData::getProductId)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(), (a, b) -> a));

        List<InventoryProcessResponseData> responseDataList = new ArrayList<>();
        for(InventoryProcessRequestData data : request.getProductList())
        {
            InventoryProcessResponseData responseData = new InventoryProcessResponseData();
            responseData.setProductId(data.getProductId());
            Product product = productMap.get(data.getProductId());
            boolean isAvailable = false;
            if(product != null)
            {
                int availableStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
                int requestedStock = data.getQuantity() != null ? data.getQuantity() : 0;
                isAvailable = requestedStock > 0 && availableStock >= requestedStock;
                if(isAvailable)
                {
                    product.setStockQuantity(availableStock - requestedStock);
                    int reserved = product.getReservedQuantity() != null ? product.getReservedQuantity() : 0;
                    product.setReservedQuantity(reserved + requestedStock);
                }
            }
            responseData.setAvailable(isAvailable);
            responseDataList.add(responseData);
        }

        InventoryProcessResponse response = new InventoryProcessResponse();
        response.setOrderId(request.getOrderId());
        response.setProductList(responseDataList);

        productRepository.saveAll(products);

        sendInventoryResponse(response);
    }

    void sendInventoryResponse(InventoryProcessResponse response)
    {
        String key = response.getOrderId() != null ? String.valueOf(response.getOrderId()) : null;
        kafkaTemplate.send("inventory-process-completed", key, response);
    }
}
