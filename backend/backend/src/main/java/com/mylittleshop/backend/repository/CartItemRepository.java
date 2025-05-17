package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
} 