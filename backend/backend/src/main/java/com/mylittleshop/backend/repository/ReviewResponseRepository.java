package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewResponseRepository extends JpaRepository<ReviewResponse, Long> {
} 