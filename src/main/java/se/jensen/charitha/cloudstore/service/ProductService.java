package se.jensen.charitha.cloudstore.service;

import se.jensen.charitha.cloudstore.client.ProductClient;
import se.jensen.charitha.cloudstore.model.Product;
import se.jensen.charitha.cloudstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductClient productClient;

    public ProductService(ProductRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public List<Product> fetchAndSaveProducts() {
        Product[] response = productClient.fetchProducts();
        List<Product> products = Arrays.asList(response != null ? response : new Product[0]);
        repository.saveAll(products);
        return repository.findAll();
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }
}
