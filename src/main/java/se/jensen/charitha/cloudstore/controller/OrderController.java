package se.jensen.charitha.cloudstore.controller;

import org.springframework.security.core.Authentication;
import se.jensen.charitha.cloudstore.dto.OrderRequestDto;
import se.jensen.charitha.cloudstore.model.Order;
import se.jensen.charitha.cloudstore.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Order createOrder(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody OrderRequestDto request) {
        return service.createOrder(request);
    }

    @GetMapping
    public List<Order> getOrders(Authentication authentication) {
        String username = authentication.getName();
        return service.getOrderForUser(username);
    }
}
