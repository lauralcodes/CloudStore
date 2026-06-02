package se.jensen.charitha.cloudstore.repository;

import se.jensen.charitha.cloudstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
