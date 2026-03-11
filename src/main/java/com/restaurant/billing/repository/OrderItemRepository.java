package com.restaurant.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.billing.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
