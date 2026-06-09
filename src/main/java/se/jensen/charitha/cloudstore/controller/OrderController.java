package se.jensen.charitha.cloudstore.controller;

import se.jensen.charitha.cloudstore.dto.OrderRequestDto;
import se.jensen.charitha.cloudstore.model.Order;
import se.jensen.charitha.cloudstore.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Order createOrder(@Valid @RequestBody OrderRequestDto request, Principal principal) {
        return service.createOrder(request, principal.getName());
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }
}
