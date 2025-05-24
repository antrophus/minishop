package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
} 