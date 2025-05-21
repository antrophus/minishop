package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.SubscriptionPlan;
import com.mylittleshop.backend.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    /**
     * 구독 플랜 저장
     * 
     * @param plan 구독 플랜
     * @return 저장된 구독 플랜
     */
    @Transactional
    public SubscriptionPlan save(SubscriptionPlan plan) {
        return subscriptionPlanRepository.save(plan);
    }

    /**
     * ID로 구독 플랜 조회
     * 
     * @param id 구독 플랜 ID
     * @return 구독 플랜 Optional
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionPlan> findById(Long id) {
        return subscriptionPlanRepository.findById(id);
    }

    /**
     * 모든 구독 플랜 조회
     * 
     * @return 구독 플랜 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPlan> findAll() {
        return subscriptionPlanRepository.findAll();
    }

    /**
     * 구독 플랜 삭제
     * 
     * @param id 구독 플랜 ID
     */
    @Transactional
    public void deleteById(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }
    
    /**
     * 이름으로 구독 플랜 조회
     * 
     * @param name 구독 플랜 이름
     * @return 구독 플랜 Optional
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionPlan> findByName(String name) {
        return subscriptionPlanRepository.findByName(name);
    }
    
    /**
     * 활성 상태의 구독 플랜 조회
     * 
     * @return 활성 구독 플랜 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPlan> findByIsActive(boolean isActive) {
        return subscriptionPlanRepository.findByIsActive(isActive);
    }
    
    /**
     * 활성 상태의 구독 플랜만 조회
     * 
     * @return 활성 구독 플랜 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPlan> findAllActive() {
        return subscriptionPlanRepository.findByIsActive(true);
    }
    
    /**
     * 가격 범위로 구독 플랜 조회
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 구독 플랜 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPlan> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return subscriptionPlanRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    /**
     * 구독 기간으로 구독 플랜 조회
     * 
     * @param durationMonths 구독 기간(월)
     * @return 구독 플랜 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPlan> findByDuration(Integer duration) {
        return subscriptionPlanRepository.findByDuration(duration);
    }
    
    /**
     * 정기 주문 포함 여부로 구독 플랜 조회
     * 
     * @param includesRecurringOrder 정기 주문 포함 여부
     * @return 구독 플랜 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPlan> findByIncludesRecurringOrder(boolean includesRecurringOrder) {
        return subscriptionPlanRepository.findByIncludesRecurringOrder(includesRecurringOrder);
    }
    
    /**
     * 구독 플랜 활성화/비활성화
     * 
     * @param id 구독 플랜 ID
     * @param active 활성화 여부
     * @return 업데이트된 구독 플랜
     */
    @Transactional
    public Optional<SubscriptionPlan> updatePlanActive(Long id, boolean active) {
        return subscriptionPlanRepository.findById(id).map(plan -> {
            plan.setIsActive(active);
            return subscriptionPlanRepository.save(plan);
        });
    }
    
    /**
     * 구독 플랜 가격 업데이트
     * 
     * @param id 구독 플랜 ID
     * @param newPrice 새 가격
     * @return 업데이트된 구독 플랜
     */
    @Transactional
    public Optional<SubscriptionPlan> updatePlanPrice(Long id, BigDecimal newPrice) {
        return subscriptionPlanRepository.findById(id).map(plan -> {
            plan.setPrice(newPrice);
            return subscriptionPlanRepository.save(plan);
        });
    }
    
    /**
     * 구독 플랜 설명 업데이트
     * 
     * @param id 구독 플랜 ID
     * @param newDescription 새 설명
     * @return 업데이트된 구독 플랜
     */
    @Transactional
    public Optional<SubscriptionPlan> updatePlanDescription(Long id, String newDescription) {
        return subscriptionPlanRepository.findById(id).map(plan -> {
            plan.setDescription(newDescription);
            return subscriptionPlanRepository.save(plan);
        });
    }
    
    /**
     * 구독 플랜 기간 업데이트
     * 
     * @param id 구독 플랜 ID
     * @param newDurationMonths 새 기간(월)
     * @return 업데이트된 구독 플랜
     */
    @Transactional
    public Optional<SubscriptionPlan> updatePlanDuration(Long id, Integer newDurationMonths) {
        return subscriptionPlanRepository.findById(id).map(plan -> {
            plan.setDuration(newDurationMonths);
            return subscriptionPlanRepository.save(plan);
        });
    }
    
    /**
     * 구독 플랜 정기 주문 포함 여부 업데이트
     * 
     * @param id 구독 플랜 ID
     * @param includesRecurringOrder 정기 주문 포함 여부
     * @return 업데이트된 구독 플랜
     */
    @Transactional
    public Optional<SubscriptionPlan> updatePlanIncludesRecurringOrder(Long id, boolean includesRecurringOrder) {
        return subscriptionPlanRepository.findById(id).map(plan -> {
            plan.setIncludesRecurringOrder(includesRecurringOrder);
            return subscriptionPlanRepository.save(plan);
        });
    }
    
    /**
     * 구독 플랜 전체 업데이트
     * 
     * @param id 구독 플랜 ID
     * @param updatedPlan 업데이트할 구독 플랜 정보
     * @return 업데이트된 구독 플랜
     */
    @Transactional
    public Optional<SubscriptionPlan> updatePlan(Long id, SubscriptionPlan updatedPlan) {
        return subscriptionPlanRepository.findById(id).map(plan -> {
            plan.setName(updatedPlan.getName());
            plan.setDescription(updatedPlan.getDescription());
            plan.setPrice(updatedPlan.getPrice());
            plan.setDuration(updatedPlan.getDuration());
            plan.setIsActive(updatedPlan.getIsActive());
            plan.setIncludesRecurringOrder(updatedPlan.getIncludesRecurringOrder());
            plan.setFeatures(updatedPlan.getFeatures());
            return subscriptionPlanRepository.save(plan);
        });
    }
    
    /**
     * 페이징으로 구독 플랜 조회
     * 
     * @param pageable 페이징 정보
     * @return 구독 플랜 페이지
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionPlan> findAll(Pageable pageable) {
        return subscriptionPlanRepository.findAll(pageable);
    }
    
    /**
     * 활성 상태의 구독 플랜 페이징 조회
     * 
     * @param pageable 페이징 정보
     * @return 활성 구독 플랜 페이지
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionPlan> findAllActive(Pageable pageable) {
        return subscriptionPlanRepository.findByIsActive(true, pageable);
    }
    
    /**
     * 새 구독 플랜 생성
     * 
     * @param name 이름
     * @param description 설명
     * @param price 가격
     * @param duration 기간(월)
     * @param features 기능 설명
     * @param includesRecurringOrder 정기 주문 포함 여부
     * @return 생성된 구독 플랜
     */
    @Transactional
    public SubscriptionPlan createPlan(String name, String description, BigDecimal price, 
            Integer duration, String features, boolean includesRecurringOrder) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setName(name);
        plan.setDescription(description);
        plan.setPrice(price);
        plan.setDuration(duration);
        plan.setFeatures(features);
        plan.setIsActive(true);
        plan.setIncludesRecurringOrder(includesRecurringOrder);
        return subscriptionPlanRepository.save(plan);
    }
}