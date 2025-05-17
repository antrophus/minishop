package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
} 