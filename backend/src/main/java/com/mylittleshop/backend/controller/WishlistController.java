package com.mylittleshop.backend.controller;

import com.mylittleshop.backend.dto.WishlistResponse;
import com.mylittleshop.backend.model.Wishlist;
import com.mylittleshop.backend.service.WishlistService;
import com.mylittleshop.backend.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistController {
    
    private final WishlistService wishlistService;
    private final DtoMapper dtoMapper;
    
    /**
     * 사용자 위시리스트 조회
     * GET /api/wishlist/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistResponse>> getUserWishlist(@PathVariable Long userId) {
        try {
            log.info("사용자 위시리스트 조회 요청: userId={}", userId);
            
            List<Wishlist> wishlistItems = wishlistService.findByUserId(userId);
            List<WishlistResponse> response = dtoMapper.toWishlistResponseList(wishlistItems);
            
            log.info("사용자 위시리스트 조회 성공: userId={}, itemCount={}", userId, response.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("사용자 위시리스트 조회 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 위시리스트에 상품 추가
     * POST /api/wishlist/{userId}/items/{productId}
     */
    @PostMapping("/{userId}/items/{productId}")
    public ResponseEntity<WishlistResponse> addToWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        try {
            log.info("위시리스트 상품 추가 요청: userId={}, productId={}", userId, productId);
            
            Wishlist wishlist = wishlistService.addToWishlist(userId, productId);
            WishlistResponse response = dtoMapper.toWishlistResponse(wishlist);
            
            log.info("위시리스트 상품 추가 성공: wishlistId={}", wishlist.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("위시리스트 상품 추가 실패 - 잘못된 요청: userId={}, productId={}, error={}", 
                    userId, productId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("위시리스트 상품 추가 실패: userId={}, productId={}, error={}", 
                    userId, productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 위시리스트에서 상품 제거
     * DELETE /api/wishlist/{userId}/items/{productId}
     */
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        try {
            log.info("위시리스트 상품 제거 요청: userId={}, productId={}", userId, productId);
            
            boolean removed = wishlistService.removeFromWishlist(userId, productId);
            
            if (removed) {
                log.info("위시리스트 상품 제거 성공: userId={}, productId={}", userId, productId);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("위시리스트 상품 제거 실패 - 아이템을 찾을 수 없음: userId={}, productId={}", 
                        userId, productId);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("위시리스트 상품 제거 실패: userId={}, productId={}, error={}", 
                    userId, productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 상품이 위시리스트에 있는지 확인
     * GET /api/wishlist/{userId}/check/{productId}
     */
    @GetMapping("/{userId}/check/{productId}")
    public ResponseEntity<Boolean> isProductInWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        try {
            log.info("위시리스트 상품 확인 요청: userId={}, productId={}", userId, productId);
            
            boolean exists = wishlistService.findByUserIdAndProductId(userId, productId).isPresent();
            
            log.info("위시리스트 상품 확인 성공: userId={}, productId={}, exists={}", 
                    userId, productId, exists);
            return ResponseEntity.ok(exists);
            
        } catch (Exception e) {
            log.error("위시리스트 상품 확인 실패: userId={}, productId={}, error={}", 
                    userId, productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 위시리스트 아이템 수 조회
     * GET /api/wishlist/{userId}/count
     */
    @GetMapping("/{userId}/count")
    public ResponseEntity<Long> getWishlistCount(@PathVariable Long userId) {
        try {
            log.info("위시리스트 아이템 수 조회 요청: userId={}", userId);
            
            long itemCount = wishlistService.countByUserId(userId);
            
            log.info("위시리스트 아이템 수 조회 성공: userId={}, count={}", userId, itemCount);
            return ResponseEntity.ok(itemCount);
            
        } catch (Exception e) {
            log.error("위시리스트 아이템 수 조회 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 위시리스트 비우기
     * DELETE /api/wishlist/{userId}/clear
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearWishlist(@PathVariable Long userId) {
        try {
            log.info("위시리스트 비우기 요청: userId={}", userId);
            
            int removedCount = wishlistService.clearWishlist(userId);
            
            log.info("위시리스트 비우기 성공: userId={}, removedCount={}", userId, removedCount);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            log.error("위시리스트 비우기 실패: userId={}, error={}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
