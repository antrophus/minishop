package com.mylittleshop.backend.controller;

import com.mylittleshop.backend.dto.AddCartItemRequest;
import com.mylittleshop.backend.dto.CartItemResponse;
import com.mylittleshop.backend.dto.UpdateQuantityRequest;
import com.mylittleshop.backend.model.CartItem;
import com.mylittleshop.backend.service.CartItemService;
import com.mylittleshop.backend.service.CartService;
import com.mylittleshop.backend.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {
    
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final DtoMapper dtoMapper;
    
    /**
     * 사용자 장바구니 조회
     * GET /api/cart/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getUserCart(@PathVariable Long userId) {
        try {
            log.info("사용자 장바구니 조회 요청: userId={}", userId);
            
            List<CartItem> cartItems = cartService.getUserCartItems(userId);
            List<CartItemResponse> response = dtoMapper.toCartItemResponseList(cartItems);
            
            log.info("사용자 장바구니 조회 성공: userId={}, itemCount={}", userId, response.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("사용자 장바구니 조회 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 장바구니에 상품 추가
     * POST /api/cart/{userId}/items
     */    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItemResponse> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddCartItemRequest request) {
        try {
            log.info("장바구니 상품 추가 요청: userId={}, productId={}, quantity={}", 
                    userId, request.getProductId(), request.getQuantity());
            
            CartItem cartItem = cartService.addItem(userId, request.getProductId(), request.getQuantity());
            CartItemResponse response = dtoMapper.toCartItemResponse(cartItem);
            
            log.info("장바구니 상품 추가 성공: cartItemId={}", cartItem.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("장바구니 상품 추가 실패 - 잘못된 요청: userId={}, error={}", userId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("장바구니 상품 추가 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 장바구니 아이템 수량 수정
     * PUT /api/cart/items/{cartItemId}
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        try {
            log.info("장바구니 아이템 수량 수정 요청: cartItemId={}, quantity={}", 
                    cartItemId, request.getQuantity());
            
            CartItem cartItem = cartItemService.updateQuantity(cartItemId, request.getQuantity());
            CartItemResponse response = dtoMapper.toCartItemResponse(cartItem);
            
            log.info("장바구니 아이템 수량 수정 성공: cartItemId={}", cartItemId);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("장바구니 아이템 수량 수정 실패 - 잘못된 요청: cartItemId={}, error={}", 
                    cartItemId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("장바구니 아이템 수량 수정 실패 - 아이템을 찾을 수 없음: cartItemId={}", cartItemId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("장바구니 아이템 수량 수정 실패: cartItemId={}, error={}", 
                    cartItemId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }    
    /**
     * 장바구니 아이템 삭제
     * DELETE /api/cart/items/{cartItemId}
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        try {
            log.info("장바구니 아이템 삭제 요청: cartItemId={}", cartItemId);
            
            cartItemService.deleteCartItem(cartItemId);
            
            log.info("장바구니 아이템 삭제 성공: cartItemId={}", cartItemId);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            log.warn("장바구니 아이템 삭제 실패 - 아이템을 찾을 수 없음: cartItemId={}", cartItemId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("장바구니 아이템 삭제 실패: cartItemId={}, error={}", cartItemId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 장바구니 비우기
     * DELETE /api/cart/{userId}/clear
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        try {
            log.info("장바구니 비우기 요청: userId={}", userId);
            
            int removedCount = cartService.clearUserCart(userId);
            
            log.info("장바구니 비우기 성공: userId={}, removedCount={}", userId, removedCount);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            log.error("장바구니 비우기 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }    
    /**
     * 장바구니 아이템 수 조회
     * GET /api/cart/{userId}/count
     */
    @GetMapping("/{userId}/count")
    public ResponseEntity<Long> getCartItemCount(@PathVariable Long userId) {
        try {
            log.info("장바구니 아이템 수 조회 요청: userId={}", userId);
            
            long itemCount = cartService.countUserCartItems(userId);
            
            log.info("장바구니 아이템 수 조회 성공: userId={}, count={}", userId, itemCount);
            return ResponseEntity.ok(itemCount);
            
        } catch (Exception e) {
            log.error("장바구니 아이템 수 조회 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 장바구니 총액 조회
     * GET /api/cart/{userId}/total
     */
    @GetMapping("/{userId}/total")
    public ResponseEntity<BigDecimal> getCartTotal(@PathVariable Long userId) {
        try {
            log.info("장바구니 총액 조회 요청: userId={}", userId);
            
            List<CartItem> cartItems = cartService.getUserCartItems(userId);
            BigDecimal totalAmount = cartItems.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            log.info("장바구니 총액 조회 성공: userId={}, total={}", userId, totalAmount);
            return ResponseEntity.ok(totalAmount);
            
        } catch (Exception e) {
            log.error("장바구니 총액 조회 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
