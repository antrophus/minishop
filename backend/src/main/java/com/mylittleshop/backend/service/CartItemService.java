package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.CartItem;
import com.mylittleshop.backend.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItem save(CartItem item) { return cartItemRepository.save(item); }
    public Optional<CartItem> findById(Long id) { return cartItemRepository.findById(id); }
    public List<CartItem> findAll() { return cartItemRepository.findAll(); }
    public void deleteById(Long id) { cartItemRepository.deleteById(id); }
} 