package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.InventoryHistory;
import com.mylittleshop.backend.repository.InventoryHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryHistoryService {
    private final InventoryHistoryRepository inventoryHistoryRepository;

    @Autowired
    public InventoryHistoryService(InventoryHistoryRepository inventoryHistoryRepository) {
        this.inventoryHistoryRepository = inventoryHistoryRepository;
    }

    public List<InventoryHistory> findAll() {
        return inventoryHistoryRepository.findAll();
    }

    public InventoryHistory save(InventoryHistory inventoryHistory) {
        return inventoryHistoryRepository.save(inventoryHistory);
    }

    // 필요에 따라 추가 메서드 구현
} 