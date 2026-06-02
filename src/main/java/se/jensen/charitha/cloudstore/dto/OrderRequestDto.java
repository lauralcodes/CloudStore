package se.jensen.charitha.cloudstore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequestDto {

    @NotBlank(message = "username is required")
    private String username;

    @NotEmpty(message = "order must contain at least one item")
    @Valid
    private List<OrderItemRequestDto> items;

    public OrderRequestDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDto> items) {
        this.items = items;
    }
}
