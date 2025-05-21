package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Order;
import com.mylittleshop.backend.model.OrderStatus;
import com.mylittleshop.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // 주문번호로 조회
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // 사용자별 주문 조회
    List<Order> findByUser(User user);
    Page<Order> findByUser(User user, Pageable pageable);
    
    // 사용자 ID별 주문 조회
    List<Order> findByUserId(Long userId);
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    // 주문 상태별 조회
    List<Order> findByStatus(OrderStatus status);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    // 사용자 및 주문 상태 조합 조회
    List<Order> findByUserAndStatus(User user, OrderStatus status);
    Page<Order> findByUserAndStatus(User user, OrderStatus status, Pageable pageable);
    
    // 사용자 ID 및 주문 상태 조합 조회
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);
    
    // 주문 생성일 범위로 조회
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 사용자별 특정 기간 주문 조회
    List<Order> findByUserAndOrderDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
    Page<Order> findByUserAndOrderDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 결제 방법별 주문 조회
    List<Order> findByPaymentMethod(String paymentMethod);
    Page<Order> findByPaymentMethod(String paymentMethod, Pageable pageable);
    
    // 배송 방법별 주문 조회
    List<Order> findByShippingMethod(String shippingMethod);
    Page<Order> findByShippingMethod(String shippingMethod, Pageable pageable);
    
    // 주문 금액 범위로 조회
    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    Page<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // 최종 금액 범위로 조회
    List<Order> findByFinalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    Page<Order> findByFinalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // 선물 주문 조회
    List<Order> findByIsGiftTrue();
    Page<Order> findByIsGiftTrue(Pageable pageable);
    
    // 정기 주문 조회
    List<Order> findByIsRecurringTrue();
    Page<Order> findByIsRecurringTrue(Pageable pageable);
    
    // 정기 주문 ID별 조회
    List<Order> findByRecurringOrderId(Long recurringOrderId);
    Page<Order> findByRecurringOrderId(Long recurringOrderId, Pageable pageable);
    
    // 쿠폰 사용 주문 조회
    List<Order> findByCouponCodeIsNotNull();
    Page<Order> findByCouponCodeIsNotNull(Pageable pageable);
    
    // 특정 쿠폰으로 생성된 주문 조회
    List<Order> findByCouponCode(String couponCode);
    Page<Order> findByCouponCode(String couponCode, Pageable pageable);
    
    // 특정 배송 ID로 조회
    Optional<Order> findByDeliveryId(Long deliveryId);
    
    // 특정 기간 내 생성된 주문 수 조회
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Long countOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // 특정 기간 내 총 매출액 조회
    @Query("SELECT SUM(o.finalAmount) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.status <> 'CANCELLED'")
    BigDecimal sumFinalAmountBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // 최근 n일간 매출 통계
    @Query("SELECT DATE(o.orderDate) as orderDay, SUM(o.finalAmount) as dailyTotal " +
           "FROM Order o " +
           "WHERE o.orderDate >= :startDate AND o.status <> 'CANCELLED' " +
           "GROUP BY DATE(o.orderDate) " +
           "ORDER BY orderDay")
    List<Object[]> getDailySalesSummary(@Param("startDate") LocalDateTime startDate);
    
    // 특정 상태의 주문 수 통계
    @Query("SELECT o.status, COUNT(o) " +
           "FROM Order o " +
           "GROUP BY o.status")
    List<Object[]> countOrdersByStatus();
}