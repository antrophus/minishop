package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
} 