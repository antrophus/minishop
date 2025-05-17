package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
} 