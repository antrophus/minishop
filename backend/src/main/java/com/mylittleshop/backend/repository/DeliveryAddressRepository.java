package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
} 