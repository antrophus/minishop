package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
} 