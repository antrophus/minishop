package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
} 