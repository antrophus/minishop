package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
} 