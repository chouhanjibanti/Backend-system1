package com.restaurant.billing.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String status;	//created,paid,cancelled
	private double totalAmount;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	private String customerEmail;
	private String paymentStatus;   // PENDING, PAID
	private String paymentMethod;   // CASH, CARD, UPI
	private String customerName;
	private String customerPhone;

	
	@OneToMany(
	        mappedBy = "order",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY
	)
	@JsonManagedReference
	@Builder.Default
	private List<OrderItem> items = new ArrayList<OrderItem>();

}
