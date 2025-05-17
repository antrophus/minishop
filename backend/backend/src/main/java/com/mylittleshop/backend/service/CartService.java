package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Cart;
import com.mylittleshop.backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart save(Cart cart) { return cartRepository.save(cart); }
    public Optional<Cart> findById(Long id) { return cartRepository.findById(id); }
    public List<Cart> findAll() { return cartRepository.findAll(); }
    public void deleteById(Long id) { cartRepository.deleteById(id); }
} 