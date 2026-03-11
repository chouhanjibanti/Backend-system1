package com.restaurant.billing.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}

