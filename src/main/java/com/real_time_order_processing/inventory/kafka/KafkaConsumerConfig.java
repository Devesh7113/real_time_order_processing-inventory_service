package com.real_time_order_processing.inventory.kafka;

import com.real_time_order_processing.inventory.dto.InventoryProcessRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig
{
    @Bean
    public ConsumerFactory<String, InventoryProcessRequest> inventoryConsumerFactory()
    {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-service-process-inventory");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        JacksonJsonDeserializer<InventoryProcessRequest> valueDeserializer =
                new JacksonJsonDeserializer<>(InventoryProcessRequest.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryProcessRequest>
    inventoryKafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<String, InventoryProcessRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(inventoryConsumerFactory());
        return factory;
    }
}
