package se.jensen.charitha.cloudstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.jensen.charitha.cloudstore.client.ProductClient;
import se.jensen.charitha.cloudstore.model.Product;
import se.jensen.charitha.cloudstore.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @MockitoBean
    private ProductClient productClient;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldFetchAndSaveProductsFromClient() {
        Product productOne = new Product(1L, "First Product", 10.0f, "Description 1", "Category", "image1.png");
        Product productTwo = new Product(2L, "Second Product", 20.0f, "Description 2", "Category", "image2.png");
        when(productClient.fetchProducts()).thenReturn(new Product[]{productOne, productTwo});

        List<Product> savedProducts = productService.fetchAndSaveProducts();

        assertThat(savedProducts).hasSize(2);
        assertThat(savedProducts).extracting(Product::getTitle).containsExactlyInAnyOrder("First Product", "Second Product");
        assertThat(productRepository.findById(1L)).isPresent();
        assertThat(productRepository.findById(2L)).isPresent();
        verify(productClient).fetchProducts();
    }

    @Test
    void shouldReturnAllProductsFromRepository() {
        Product product = new Product(100L, "Stored Product", 5.0f, "Stored description", "Category", "image.png");
        productRepository.save(product);

        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getId()).isEqualTo(100L);
        assertThat(products.get(0).getTitle()).isEqualTo("Stored Product");
    }
}
