package se.jensen.charitha.cloudstore.controller;

import se.jensen.charitha.cloudstore.model.Product;
import se.jensen.charitha.cloudstore.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/fetch")
    public List<Product> fetchProducts() {
        return service.fetchAndSaveProducts();
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }
}
