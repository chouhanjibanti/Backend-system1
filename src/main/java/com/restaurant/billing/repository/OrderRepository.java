package com.restaurant.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.billing.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
}
