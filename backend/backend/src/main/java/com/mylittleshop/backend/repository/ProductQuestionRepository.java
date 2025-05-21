package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ProductQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQuestionRepository extends JpaRepository<ProductQuestion, Long> {
} 