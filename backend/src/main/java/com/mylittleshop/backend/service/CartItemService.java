package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.CartItem;
import com.mylittleshop.backend.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItem save(CartItem item) { return cartItemRepository.save(item); }
    public Optional<CartItem> findById(Long id) { return cartItemRepository.findById(id); }
    public List<CartItem> findAll() { return cartItemRepository.findAll(); }
    public void deleteById(Long id) { cartItemRepository.deleteById(id); }
    
    /**
     * 장바구니 아이템 수량 업데이트
     */
    @Transactional
    public CartItem updateQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));
        
        cartItem.setQuantity(quantity);
        cartItem.setUpdatedAt(LocalDateTime.now());
        
        return cartItemRepository.save(cartItem);
    }
    
    /**
     * 장바구니 아이템 삭제
     */
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("장바구니 아이템을 찾을 수 없습니다.");
        }
        cartItemRepository.deleteById(cartItemId);
    }
} 