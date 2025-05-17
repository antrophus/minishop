package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
} 