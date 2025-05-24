package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Cart;
import com.mylittleshop.backend.model.CartStatus;
import com.mylittleshop.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // 사용자별 장바구니 조회
    List<Cart> findByUser(User user);
    Page<Cart> findByUser(User user, Pageable pageable);
    
    // 사용자 ID별 장바구니 조회
    List<Cart> findByUserId(Long userId);
    Page<Cart> findByUserId(Long userId, Pageable pageable);
    
    // 사용자 및 상태 조합 조회
    Optional<Cart> findByUserAndStatus(User user, CartStatus status);
    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status);
    
    // 상태별 장바구니 조회
    List<Cart> findByStatus(CartStatus status);
    Page<Cart> findByStatus(CartStatus status, Pageable pageable);
    
    // 특정 기간 내에 생성된 장바구니 조회
    List<Cart> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Cart> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 특정 기간 내에 업데이트된 장바구니 조회
    List<Cart> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Cart> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 특정 상태 및 업데이트 날짜 조건 조합 조회
    List<Cart> findByStatusAndUpdatedAtBefore(CartStatus status, LocalDateTime date);
    Page<Cart> findByStatusAndUpdatedAtBefore(CartStatus status, LocalDateTime date, Pageable pageable);
    
    // 특정 날짜 이후 사용자별 장바구니 조회
    List<Cart> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date);
    Page<Cart> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date, Pageable pageable);
    
    // 빈 장바구니 조회 (아이템이 없는 장바구니)
    @Query("SELECT c FROM Cart c LEFT JOIN CartItem ci ON ci.cart = c WHERE ci.id IS NULL AND c.status = :status")
    List<Cart> findEmptyCartsByStatus(@Param("status") CartStatus status);
    
    // 아이템 수 범위로 장바구니 조회
    @Query("SELECT c FROM Cart c JOIN CartItem ci ON ci.cart = c GROUP BY c HAVING COUNT(ci) BETWEEN :minItems AND :maxItems")
    List<Cart> findByItemCountBetween(
            @Param("minItems") int minItems, 
            @Param("maxItems") int maxItems);
    
    // 총액 범위로 장바구니 조회
    @Query("SELECT c FROM Cart c JOIN CartItem ci ON ci.cart = c GROUP BY c HAVING SUM(ci.quantity * ci.unitPrice) BETWEEN :minTotal AND :maxTotal")
    List<Cart> findByTotalBetween(
            @Param("minTotal") BigDecimal minTotal, 
            @Param("maxTotal") BigDecimal maxTotal);
    
    // 특정 사용자의 장바구니 개수 조회
    long countByUserId(Long userId);
    
    // 특정 상태의 장바구니 개수 조회
    long countByStatus(CartStatus status);
    
    // 특정 기간 내의 장바구니 개수 조회
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // 특정 기간 내의 특정 상태 장바구니 개수 조회
    long countByStatusAndCreatedAtBetween(CartStatus status, LocalDateTime startDate, LocalDateTime endDate);
}