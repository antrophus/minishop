package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
} 