package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
} 