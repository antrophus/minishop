package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.SubscriptionPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment, Long> {
} 