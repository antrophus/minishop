package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Inventory;
import com.mylittleshop.backend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public Inventory save(Inventory inventory) { return inventoryRepository.save(inventory); }
    public Optional<Inventory> findById(Long id) { return inventoryRepository.findById(id); }
    public List<Inventory> findAll() { return inventoryRepository.findAll(); }
    public void deleteById(Long id) { inventoryRepository.deleteById(id); }
} 