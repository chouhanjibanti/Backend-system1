package com.restaurant.billing.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.restaurant.billing.entity.Order;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class OrderItemRequest {

    private Long menuItemId;
    private int quantity;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

}
