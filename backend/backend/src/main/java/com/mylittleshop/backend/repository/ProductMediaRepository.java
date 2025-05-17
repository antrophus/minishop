package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, Long> {
} 