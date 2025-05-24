package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Subscription;
import com.mylittleshop.backend.model.SubscriptionPlan;
import com.mylittleshop.backend.model.SubscriptionStatus;
import com.mylittleshop.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    
    // 사용자별 구독 조회
    List<Subscription> findByUser(User user);
    Page<Subscription> findByUser(User user, Pageable pageable);
    
    // 사용자 ID별 구독 조회
    List<Subscription> findByUserId(Long userId);
    Page<Subscription> findByUserId(Long userId, Pageable pageable);
    
    // 상태별 구독 조회
    List<Subscription> findByStatus(SubscriptionStatus status);
    Page<Subscription> findByStatus(SubscriptionStatus status, Pageable pageable);
    
    // 구독 플랜별 구독 조회
    List<Subscription> findByPlan(SubscriptionPlan plan);
    Page<Subscription> findByPlan(SubscriptionPlan plan, Pageable pageable);
    
    // 구독 플랜 ID별 구독 조회
    List<Subscription> findByPlanId(Long planId);
    Page<Subscription> findByPlanId(Long planId, Pageable pageable);
    
    // 시작일 기준 구독 조회 (필드명 수정)
    List<Subscription> findByContractStartDate(LocalDate contractStartDate);
    List<Subscription> findByContractStartDateAfter(LocalDate date);
    List<Subscription> findByContractStartDateBefore(LocalDate date);
    List<Subscription> findByContractStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    // 종료일 기준 구독 조회 (필드명 수정)
    List<Subscription> findByContractEndDate(LocalDate contractEndDate);
    List<Subscription> findByContractEndDateAfter(LocalDate date);
    List<Subscription> findByContractEndDateBefore(LocalDate date);
    List<Subscription> findByContractEndDateBetween(LocalDate startDate, LocalDate endDate);
    
    // 다음 결제일 기준 구독 조회
    List<Subscription> findByNextBillingDate(LocalDate billingDate);
    List<Subscription> findByNextBillingDateLessThanEqual(LocalDate date);
    
    // 특정 날짜 범위 내에 활성화된 구독 조회 (JPQL 쿼리 필드명 수정)
    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND " +
           "(:startDate BETWEEN s.contractStartDate AND s.contractEndDate OR :endDate BETWEEN s.contractStartDate AND s.contractEndDate OR " +
           "(s.contractStartDate <= :startDate AND s.contractEndDate >= :endDate))")
    List<Subscription> findActiveSubscriptionsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    // 특정 날짜에 만료되는 구독 조회
    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND s.contractEndDate = :expiryDate")
    List<Subscription> findExpiringSubscriptions(@Param("expiryDate") LocalDate expiryDate);
    
    // 특정 날짜에 결제가 예정된 구독 조회
    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND s.nextBillingDate = :billingDate")
    List<Subscription> findSubscriptionsDueForBilling(@Param("billingDate") LocalDate billingDate);
    
    // 특정 기간 내에 생성된 구독 조회
    @Query("SELECT s FROM Subscription s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    List<Subscription> findSubscriptionsCreatedBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    // 특정 기간의 구독 통계
    @Query("SELECT s.status, COUNT(s) FROM Subscription s " +
           "WHERE s.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY s.status")
    List<Object[]> getSubscriptionStatusStatistics(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    // 구독 플랜별 구독 수 통계
    @Query("SELECT s.plan.id, s.plan.name, COUNT(s) FROM Subscription s " +
           "WHERE s.status = 'ACTIVE' " +
           "GROUP BY s.plan.id, s.plan.name")
    List<Object[]> getSubscriptionCountByPlan();
} 