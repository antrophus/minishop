package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
} 