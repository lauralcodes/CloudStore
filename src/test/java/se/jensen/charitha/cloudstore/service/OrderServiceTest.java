package se.jensen.charitha.cloudstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.jensen.charitha.cloudstore.dto.OrderItemRequestDto;
import se.jensen.charitha.cloudstore.dto.OrderRequestDto;
import se.jensen.charitha.cloudstore.model.Order;
import se.jensen.charitha.cloudstore.model.Product;
import se.jensen.charitha.cloudstore.repository.OrderRepository;
import se.jensen.charitha.cloudstore.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @MockitoBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderWhenProductExists() {
        Product product = new Product(1L, "Test Product", 12.5f, "Test description", "Category", "image.png");
        productRepository.save(product);

        OrderItemRequestDto itemRequest = new OrderItemRequestDto();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        OrderRequestDto request = new OrderRequestDto();
        request.setUsername("johndoe");
        request.setItems(List.of(itemRequest));

        Order createdOrder = orderService.createOrder(request, "johndoe");

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getUsername()).isEqualTo("johndoe");
        assertThat(createdOrder.getStatus()).isEqualTo("NEW");
        assertThat(createdOrder.getTotal()).isEqualTo(25.0f);
        assertThat(createdOrder.getItems()).hasSize(1);
        assertThat(createdOrder.getItems().get(0).getProductId()).isEqualTo(1L);

        assertThat(orderService.getAllOrders()).hasSize(1);
    }

    @Test
    void shouldFetchProductsWhenRepositoryIsEmpty() {
        Product fetchedProduct = new Product(1L, "Fetched Product", 15f, "Fetched description", "Category", "image.png");
        when(productService.fetchAndSaveProducts()).thenAnswer(invocation -> productRepository.saveAll(List.of(fetchedProduct)));

        OrderItemRequestDto itemRequest = new OrderItemRequestDto();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(1);

        OrderRequestDto request = new OrderRequestDto();
        request.setUsername("janedoe");
        request.setItems(List.of(itemRequest));

        Order createdOrder = orderService.createOrder(request, "janedoe");

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getUsername()).isEqualTo("janedoe");
        assertThat(createdOrder.getTotal()).isEqualTo(15.0f);
        assertThat(createdOrder.getItems()).hasSize(1);

        verify(productService).fetchAndSaveProducts();
    }
}
