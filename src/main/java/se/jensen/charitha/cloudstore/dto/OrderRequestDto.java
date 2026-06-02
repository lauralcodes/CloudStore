package se.jensen.charitha.cloudstore.dto;

import java.util.List;

public class OrderRequestDto {

    private String username;
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
