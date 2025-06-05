package com.springKafka.oder_service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springKafka.base_domains.dto.Order;
import com.springKafka.base_domains.dto.OrderEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "order_topics" })  // Replace with your topic
public class OrderServiceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private BlockingQueue<OrderEvent> records;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.records = new LinkedBlockingQueue<>();
    }

    // Test-specific listener to verify messages reach Kafka topic
    @KafkaListener(topics = "your-topic-name", groupId = "test-group")
    public void listen(OrderEvent orderEvent) {
        records.add(orderEvent);
    }

    @Test
    public void testOrderPlacedAndConsumed() throws Exception {
        Order order = new Order();
        order.setName("Test Item");
        order.setQty(10);
        order.setPrice(25.5);
        order.setCustomerEmail("test@example.com");

        // Set other fields if required

        String orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType("application/json")
                        .content(orderJson))
                .andExpect(status().isOk());

        OrderEvent receivedEvent = records.poll(10, TimeUnit.SECONDS);

        assertThat(receivedEvent).isNotNull();
        assertThat(receivedEvent.getOrder()).isNotNull();
        assertThat(receivedEvent.getOrder().getName()).isEqualTo("Test Item");
        assertThat(receivedEvent.getOrder().getQty()).isEqualTo("test ");
                //getItem()).isEqualTo("Test Item");
    }
}
