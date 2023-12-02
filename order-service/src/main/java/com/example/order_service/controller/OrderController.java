package com.example.order_service.controller;

import com.example.base_domains.dto.OrderDto;
import com.example.base_domains.dto.OrderEvent;
import com.example.order_service.kafka.OrderProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/") // @RequestBody => Convert JSON object into Java Object
    public String placeOrder(@RequestBody OrderDto orderDto) {

        orderDto.setOrderId(UUID.randomUUID().toString());

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("order status is in pending state");
        orderEvent.setOrderDto(orderDto);

        orderProducer.sendMessage(orderEvent);

        return "Order placed successfully";
    }
}
