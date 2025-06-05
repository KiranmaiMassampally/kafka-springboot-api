package com.springKafka.email_service.kafka;

import com.springKafka.base_domains.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import com.springKafka.email_service.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
    private final EmailService emailService;

    public OrderConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent event) {
        logger.info(String.format("Order event received in email service => %s", event.toString()));

        // Send email to the consumer
        emailService.sendOrderConfirmation(event);
    }
}
