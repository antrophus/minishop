package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Subscription;
import com.mylittleshop.backend.model.SubscriptionPayment;
import com.mylittleshop.backend.model.PaymentStatus;
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

public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment, Long> {
    
    // 구독별 결제 조회
    List<SubscriptionPayment> findBySubscription(Subscription subscription);
    Page<SubscriptionPayment> findBySubscription(Subscription subscription, Pageable pageable);
    
    // 구독 ID별 결제 조회
    List<SubscriptionPayment> findBySubscriptionId(Long subscriptionId);
    Page<SubscriptionPayment> findBySubscriptionId(Long subscriptionId, Pageable pageable);
    
    // 구독별 최근 결제 조회
    Optional<SubscriptionPayment> findTopBySubscriptionOrderByPaymentDateDesc(Subscription subscription);
    Optional<SubscriptionPayment> findTopBySubscriptionIdOrderByPaymentDateDesc(Long subscriptionId);
    
    // 사용자별 결제 조회
    List<SubscriptionPayment> findBySubscriptionUser(User user);
    Page<SubscriptionPayment> findBySubscriptionUser(User user, Pageable pageable);
    
    // 사용자 ID별 결제 조회
    List<SubscriptionPayment> findBySubscriptionUserId(Long userId);
    Page<SubscriptionPayment> findBySubscriptionUserId(Long userId, Pageable pageable);
    
    // 결제 상태별 결제 조회
    List<SubscriptionPayment> findByStatus(PaymentStatus status);
    Page<SubscriptionPayment> findByStatus(PaymentStatus status, Pageable pageable);
    
    // 결제일 범위별 결제 조회
    List<SubscriptionPayment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<SubscriptionPayment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 결제액 범위별 결제 조회
    List<SubscriptionPayment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    Page<SubscriptionPayment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // 결제일 기준 결제 건수 조회
    long countByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // 사용자별 기간 결제 총액 조회
    @Query("SELECT SUM(p.amount) FROM SubscriptionPayment p " +
           "WHERE p.subscription.user.id = :userId " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate " +
           "AND p.status = 'COMPLETED'")
    BigDecimal sumPaymentAmountByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    // 기간별 결제 총액 조회
    @Query("SELECT SUM(p.amount) FROM SubscriptionPayment p " +
           "WHERE p.paymentDate BETWEEN :startDate AND :endDate " +
           "AND p.status = 'COMPLETED'")
    BigDecimal sumPaymentAmountByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    // 일별 결제 통계 조회
    @Query("SELECT CAST(p.paymentDate AS DATE), COUNT(p), SUM(p.amount) " +
           "FROM SubscriptionPayment p " +
           "WHERE p.paymentDate BETWEEN :startDate AND :endDate " +
           "AND p.status = 'COMPLETED' " +
           "GROUP BY CAST(p.paymentDate AS DATE) " +
           "ORDER BY CAST(p.paymentDate AS DATE)")
    List<Object[]> getDailyPaymentStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    // 월별 결제 통계 조회
    @Query("SELECT MONTH(p.paymentDate), COUNT(p), SUM(p.amount) " +
           "FROM SubscriptionPayment p " +
           "WHERE YEAR(p.paymentDate) = :year " +
           "AND p.status = 'COMPLETED' " +
           "GROUP BY MONTH(p.paymentDate) " +
           "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> getMonthlyPaymentStatistics(@Param("year") int year);
    
    // 결제 수단별 결제 조회
    List<SubscriptionPayment> findByPaymentMethodId(Long paymentMethodId);
    Page<SubscriptionPayment> findByPaymentMethodId(Long paymentMethodId, Pageable pageable);
    
    // 결제 수단별 결제 총액 조회
    @Query("SELECT p.paymentMethodId, SUM(p.amount) " +
           "FROM SubscriptionPayment p " +
           "WHERE p.status = 'COMPLETED' " +
           "GROUP BY p.paymentMethodId")
    List<Object[]> sumPaymentAmountByPaymentMethod();
} 