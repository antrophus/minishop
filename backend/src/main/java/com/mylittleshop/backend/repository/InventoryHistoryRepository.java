package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Long> {
} 