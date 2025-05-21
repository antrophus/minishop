package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    
    // 이름으로 구독 플랜 조회
    Optional<SubscriptionPlan> findByName(String name);
    
    // 활성 상태별 구독 플랜 조회
    List<SubscriptionPlan> findByIsActive(boolean isActive);
    Page<SubscriptionPlan> findByIsActive(boolean isActive, Pageable pageable);
    
    // 가격 범위별 구독 플랜 조회
    List<SubscriptionPlan> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    Page<SubscriptionPlan> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    // 구독 기간별 구독 플랜 조회
    List<SubscriptionPlan> findByDuration(Integer duration);
    Page<SubscriptionPlan> findByDuration(Integer duration, Pageable pageable);
    
    // 정기 주문 포함 여부별 구독 플랜 조회
    List<SubscriptionPlan> findByIncludesRecurringOrder(boolean includesRecurringOrder);
    Page<SubscriptionPlan> findByIncludesRecurringOrder(boolean includesRecurringOrder, Pageable pageable);
    
    // 이름 유사도로 구독 플랜 검색
    List<SubscriptionPlan> findByNameContaining(String nameSubstring);
    Page<SubscriptionPlan> findByNameContaining(String nameSubstring, Pageable pageable);
    
    // 설명 유사도로 구독 플랜 검색
    List<SubscriptionPlan> findByDescriptionContaining(String descriptionSubstring);
    Page<SubscriptionPlan> findByDescriptionContaining(String descriptionSubstring, Pageable pageable);
    
    // 가격 오름차순 정렬
    List<SubscriptionPlan> findByIsActiveOrderByPriceAsc(boolean isActive);
    Page<SubscriptionPlan> findByIsActiveOrderByPriceAsc(boolean isActive, Pageable pageable);
    
    // 가격 내림차순 정렬
    List<SubscriptionPlan> findByIsActiveOrderByPriceDesc(boolean isActive);
    Page<SubscriptionPlan> findByIsActiveOrderByPriceDesc(boolean isActive, Pageable pageable);
    
    // 구독 기간 오름차순 정렬
    List<SubscriptionPlan> findByIsActiveOrderByDurationAsc(boolean isActive);
    Page<SubscriptionPlan> findByIsActiveOrderByDurationAsc(boolean isActive, Pageable pageable);
    
    // 사용자가 구독 가능한 플랜 조회 (활성 상태)
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.isActive = true ORDER BY p.price")
    List<SubscriptionPlan> findAvailablePlans();
    
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.isActive = true ORDER BY p.price")
    Page<SubscriptionPlan> findAvailablePlans(Pageable pageable);
    
    // 가장 인기 있는 구독 플랜 조회 (구독 수 기준)
    @Query("SELECT p, COUNT(s) as subscriptionCount " +
           "FROM SubscriptionPlan p " +
           "LEFT JOIN p.subscriptions s " +
           "WHERE p.isActive = true " +
           "GROUP BY p " +
           "ORDER BY subscriptionCount DESC")
    List<Object[]> findMostPopularPlans();
    
    // 특정 가격 이하의 구독 플랜 조회
    List<SubscriptionPlan> findByPriceLessThanEqual(BigDecimal maxPrice);
    Page<SubscriptionPlan> findByPriceLessThanEqual(BigDecimal maxPrice, Pageable pageable);
    
    // 특정 가격 이상의 구독 플랜 조회
    List<SubscriptionPlan> findByPriceGreaterThanEqual(BigDecimal minPrice);
    Page<SubscriptionPlan> findByPriceGreaterThanEqual(BigDecimal minPrice, Pageable pageable);
} 