package com.springKafka.stock_service.kafka;

import com.springKafka.base_domains.dto.OrderEvent;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class orderConsumer {

    private static Logger logger = LoggerFactory.getLogger(orderConsumer.class);

    @KafkaListener(topics="${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent event) {
logger.info(String.format("Order event received in stock service => %s", event.toString()));

//save the order event into the database

    }
}
