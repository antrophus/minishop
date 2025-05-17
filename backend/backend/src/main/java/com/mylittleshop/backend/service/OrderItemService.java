package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.OrderItem;
import com.mylittleshop.backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItem save(OrderItem item) { return orderItemRepository.save(item); }
    public Optional<OrderItem> findById(Long id) { return orderItemRepository.findById(id); }
    public List<OrderItem> findAll() { return orderItemRepository.findAll(); }
    public void deleteById(Long id) { orderItemRepository.deleteById(id); }
} 