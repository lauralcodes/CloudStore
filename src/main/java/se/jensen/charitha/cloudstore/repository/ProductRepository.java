package se.jensen.charitha.cloudstore.repository;

import se.jensen.charitha.cloudstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
