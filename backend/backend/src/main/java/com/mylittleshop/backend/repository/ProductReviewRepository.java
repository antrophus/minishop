package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
} 