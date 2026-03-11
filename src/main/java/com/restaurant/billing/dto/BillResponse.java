package com.restaurant.billing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BillResponse {
	private double subtotal;
	private double tax;
	private double discount;
	private double grandTotal;
}
