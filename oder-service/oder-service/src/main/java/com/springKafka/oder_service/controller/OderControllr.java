package com.springKafka.oder_service.controller;

import com.springKafka.base_domains.dto.Order;
import com.springKafka.base_domains.dto.OrderEvent;
import com.springKafka.oder_service.kafka.OrderProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class OderControllr {

    private OrderProducer orderProducer;

    //constructer based dependecny injection

    public OderControllr(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }
    @PostMapping("/orders")
    //requestBody -->json obj- java obj
    public String placeOrder(@RequestBody Order order) {
        order.setOrderId(UUID.randomUUID().toString());

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("Order status is in pending state");
        orderEvent.setOrder(order);
        orderProducer.sendMessage(orderEvent);
        return "Oder Placed successfully";

    }
}
