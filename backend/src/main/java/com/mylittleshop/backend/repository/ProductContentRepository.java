package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductContentRepository extends JpaRepository<ProductContent, Long> {
    Optional<ProductContent> findByProductId(Long productId);
} 