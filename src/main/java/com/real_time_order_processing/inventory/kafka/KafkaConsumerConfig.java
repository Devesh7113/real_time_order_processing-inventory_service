package com.real_time_order_processing.inventory.kafka;

import com.real_time_order_processing.inventory.dto.InventoryProcessRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig
{
    @Bean
    public ConsumerFactory<String, InventoryProcessRequest> inventoryConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-service-process-inventory");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<InventoryProcessRequest> valueDeserializer = new JsonDeserializer<>(InventoryProcessRequest.class);
        valueDeserializer.addTrustedPackages("com.real_time_order_processing.inventory");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryProcessRequest>
    inventoryKafkaListenerContainerFactory(
            ConsumerFactory<String, InventoryProcessRequest> inventoryConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, InventoryProcessRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(inventoryConsumerFactory);
        return factory;
    }
}
