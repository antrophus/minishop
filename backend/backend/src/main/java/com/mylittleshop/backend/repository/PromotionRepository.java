package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
} 