package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Order;
import com.mylittleshop.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order save(Order order) { return orderRepository.save(order); }
    public Optional<Order> findById(Long id) { return orderRepository.findById(id); }
    public List<Order> findAll() { return orderRepository.findAll(); }
    public void deleteById(Long id) { orderRepository.deleteById(id); }
} 