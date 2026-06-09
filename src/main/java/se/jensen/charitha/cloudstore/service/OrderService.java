package se.jensen.charitha.cloudstore.service;

import se.jensen.charitha.cloudstore.dto.OrderItemRequestDto;
import se.jensen.charitha.cloudstore.dto.OrderRequestDto;
import se.jensen.charitha.cloudstore.model.Order;
import se.jensen.charitha.cloudstore.model.OrderItem;
import se.jensen.charitha.cloudstore.model.Product;
import se.jensen.charitha.cloudstore.repository.OrderRepository;
import se.jensen.charitha.cloudstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public Order createOrder(OrderRequestDto request, String authenticatedUsername) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required to place an order.");
        }
        if (!request.getUsername().equals(authenticatedUsername)) {
            throw new IllegalArgumentException("Authenticated user does not match order username.");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("At least one order item is required.");
        }

        if (productRepository.count() == 0) {
            productService.fetchAndSaveProducts();
        }

        Order order = new Order(request.getUsername(), 0f, "NEW");
        float total = 0f;

        for (OrderItemRequestDto itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.getProductId()));
            int quantity = Math.max(itemRequest.getQuantity(), 1);
            float itemTotal = product.getPrice() * quantity;
            OrderItem orderItem = new OrderItem(product.getId(), product.getTitle(), product.getPrice(), quantity, itemTotal);
            order.addItem(orderItem);
            total += itemTotal;
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }

    public List<Order> getOrderForUser(String username) {
        return orderRepository.findByUsername(username);
    }
}
