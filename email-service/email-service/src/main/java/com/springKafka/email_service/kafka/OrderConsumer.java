package com.springKafka.email_service.kafka;

import com.springKafka.base_domains.dto.OrderEvent;
import com.springKafka.email_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
    private final EmailService emailService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String jenkinsUrl = "http://localhost:8080/job/your-job-name/build?token=your-token"; // Replace placeholders

    public OrderConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent event) {
        logger.info(String.format("Order event received in email service => %s", event.toString()));
        emailService.sendOrderConfirmation(event);

        try {
            String jenkinsUrl = "http://localhost:8080/job/your-job-name/build?token=your-token";
            restTemplate.postForObject(jenkinsUrl, null, String.class);
            logger.info("Jenkins build triggered successfully.");
        } catch (Exception e) {
            logger.error("Failed to trigger Jenkins job", e);
        }
    }

}
