package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    
    // 코드로 프로모션 조회
    Optional<Promotion> findByCode(String code);
    
    // 활성 상태별 프로모션 조회
    List<Promotion> findByActive(boolean active);
    Page<Promotion> findByActive(boolean active, Pageable pageable);
    
    // 유형별 프로모션 조회
    List<Promotion> findByType(String type);
    Page<Promotion> findByType(String type, Pageable pageable);
    
    // 현재 활성화된 프로모션 조회 (시작일 <= 현재 <= 종료일)
    List<Promotion> findByStartDateBeforeAndEndDateAfterAndActive(
            LocalDateTime currentTime, 
            LocalDateTime currentTime2, 
            boolean active);
            
    Page<Promotion> findByStartDateBeforeAndEndDateAfterAndActive(
            LocalDateTime currentTime, 
            LocalDateTime currentTime2, 
            boolean active, 
            Pageable pageable);
    
    // 유형별 현재 활성화된 프로모션 조회
    List<Promotion> findByTypeAndStartDateBeforeAndEndDateAfterAndActive(
            String type,
            LocalDateTime currentTime, 
            LocalDateTime currentTime2, 
            boolean active);
            
    Page<Promotion> findByTypeAndStartDateBeforeAndEndDateAfterAndActive(
            String type,
            LocalDateTime currentTime, 
            LocalDateTime currentTime2, 
            boolean active, 
            Pageable pageable);
    
    // 코드별 현재 활성화된 프로모션 조회
    Optional<Promotion> findByCodeAndStartDateBeforeAndEndDateAfterAndActive(
            String code,
            LocalDateTime currentTime, 
            LocalDateTime currentTime2, 
            boolean active);
    
    // 특정 기간의 프로모션 조회
    List<Promotion> findByStartDateAfterAndEndDateBefore(LocalDateTime startDate, LocalDateTime endDate);
    Page<Promotion> findByStartDateAfterAndEndDateBefore(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 특정 카테고리에 적용 가능한 프로모션 조회
    @Query("SELECT p FROM Promotion p JOIN p.categoryIds c WHERE c = :categoryId AND p.active = true")
    List<Promotion> findByCategoryIdsContaining(@Param("categoryId") String categoryId);
    
    // 특정 상품에 적용 가능한 프로모션 조회
    @Query("SELECT p FROM Promotion p JOIN p.productIds pid WHERE pid = :productId AND p.active = true")
    List<Promotion> findByProductIdsContaining(@Param("productId") String productId);
    
    // 특정 사용자에게 적용 가능한 프로모션 조회
    @Query("SELECT p FROM Promotion p JOIN p.userIds uid WHERE uid = :userId AND p.active = true")
    List<Promotion> findByUserIdsContaining(@Param("userId") String userId);
    
    // 이름으로 프로모션 검색
    List<Promotion> findByNameContaining(String name);
    Page<Promotion> findByNameContaining(String name, Pageable pageable);
    
    // 활성화된 프로모션 중 할인 유형별 검색
    List<Promotion> findByDiscountTypeAndActive(String discountType, boolean active);
    Page<Promotion> findByDiscountTypeAndActive(String discountType, boolean active, Pageable pageable);
    
    // 만료 예정인 프로모션 조회 (오늘 기준 n일 이내 만료)
    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.endDate BETWEEN :today AND :futureDate")
    List<Promotion> findExpiringPromotions(
            @Param("today") LocalDateTime today, 
            @Param("futureDate") LocalDateTime futureDate);
    
    // 만료된 프로모션 조회 (오늘 기준 이미 만료됨)
    @Query("SELECT p FROM Promotion p WHERE p.endDate < :today")
    List<Promotion> findExpiredPromotions(@Param("today") LocalDateTime today);
    
    // 특정 최소 주문 금액 이하의 프로모션 조회
    List<Promotion> findByMinimumOrderAmountLessThanEqual(BigDecimal amount);
    Page<Promotion> findByMinimumOrderAmountLessThanEqual(BigDecimal amount, Pageable pageable);
    
    // 사용 횟수가 최대 사용 횟수의 n% 이상인 프로모션 조회
    @Query("SELECT p FROM Promotion p WHERE p.maxUsageCount IS NOT NULL AND p.currentUsageCount >= (p.maxUsageCount * :percentage / 100)")
    List<Promotion> findByUsagePercentage(@Param("percentage") double percentage);
}