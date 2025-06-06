package com.springKafka.oder_service.kafka;

import com.springKafka.base_domains.dto.OrderEvent;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    //service will config class as bean and rgister as spring IOc container

//instacne varilables
    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);
    private NewTopic topic;
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(NewTopic topic,KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(OrderEvent event) {

log.info(String.format("Sending message to topic %s", event.toString()));
//creater Message here
Message<OrderEvent> message = MessageBuilder
        .withPayload(event)
        .setHeader(KafkaHeaders.TOPIC, topic.name())
        .build();

        kafkaTemplate.send(message);
    }
}
