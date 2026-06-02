package se.jensen.charitha.cloudstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequestDto {

    @NotNull(message = "productId is required")
    private Long productId;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    public OrderItemRequestDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
