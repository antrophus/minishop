package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
} 