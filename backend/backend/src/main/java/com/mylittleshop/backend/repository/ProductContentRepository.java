package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductContentRepository extends JpaRepository<ProductContent, Long> {
} 