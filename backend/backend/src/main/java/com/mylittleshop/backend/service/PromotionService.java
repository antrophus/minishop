package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Order;
import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.Promotion;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    /**
     * 프로모션 저장
     * 
     * @param promotion 프로모션
     * @return 저장된 프로모션
     */
    @Transactional
    public Promotion save(Promotion promotion) { 
        return promotionRepository.save(promotion); 
    }
    
    /**
     * ID로 프로모션 조회
     * 
     * @param id 프로모션 ID
     * @return 프로모션 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Promotion> findById(Long id) { 
        return promotionRepository.findById(id); 
    }
    
    /**
     * 모든 프로모션 조회
     * 
     * @return 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findAll() { 
        return promotionRepository.findAll(); 
    }
    
    /**
     * 프로모션 삭제
     * 
     * @param id 프로모션 ID
     */
    @Transactional
    public void deleteById(Long id) { 
        promotionRepository.deleteById(id); 
    }
    
    /**
     * 현재 활성화된 모든 프로모션 조회
     * 
     * @return 활성화된 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findAllActive() {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByStartDateBeforeAndEndDateAfterAndActive(now, now, true);
    }
    
    /**
     * 현재 활성화된 프로모션 페이징 조회
     * 
     * @param pageable 페이징 정보
     * @return 활성화된 프로모션 페이지
     */
    @Transactional(readOnly = true)
    public Page<Promotion> findAllActive(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByStartDateBeforeAndEndDateAfterAndActive(now, now, true, pageable);
    }
    
    /**
     * 코드로 프로모션 조회
     * 
     * @param code 프로모션 코드
     * @return 프로모션 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Promotion> findByCode(String code) {
        return promotionRepository.findByCode(code);
    }
    
    /**
     * 현재 활성화된 특정 코드의 프로모션 조회
     * 
     * @param code 프로모션 코드
     * @return 활성화된 프로모션 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Promotion> findActiveByCode(String code) {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByCodeAndStartDateBeforeAndEndDateAfterAndActive(code, now, now, true);
    }
    
    /**
     * 특정 유형의 프로모션 조회
     * 
     * @param type 프로모션 유형
     * @return 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findByType(String type) {
        return promotionRepository.findByType(type);
    }
    
    /**
     * 현재 활성화된 특정 유형의 프로모션 조회
     * 
     * @param type 프로모션 유형
     * @return 활성화된 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findActiveByType(String type) {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByTypeAndStartDateBeforeAndEndDateAfterAndActive(type, now, now, true);
    }
    
    /**
     * 특정 카테고리에 적용 가능한 프로모션 조회
     * 
     * @param categoryId 카테고리 ID
     * @return 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findByCategoryId(Long categoryId) {
        return promotionRepository.findByCategoryIdsContaining(categoryId.toString());
    }
    
    /**
     * 특정 상품에 적용 가능한 프로모션 조회
     * 
     * @param productId 상품 ID
     * @return 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findByProductId(Long productId) {
        return promotionRepository.findByProductIdsContaining(productId.toString());
    }
    
    /**
     * 특정 사용자가 사용 가능한 프로모션 조회
     * 
     * @param userId 사용자 ID
     * @return 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        
        // 현재 활성화된 모든 프로모션 조회
        List<Promotion> activePromotions = promotionRepository.findByStartDateBeforeAndEndDateAfterAndActive(now, now, true);
        
        // 사용자에게 적용 가능한 프로모션만 필터링
        return activePromotions.stream()
                .filter(promotion -> isPromotionApplicableToUser(promotion, userId))
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 상품에 적용 가능한 활성화된 프로모션 조회
     * 
     * @param product 상품
     * @return 활성화된 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findActiveByProduct(Product product) {
        LocalDateTime now = LocalDateTime.now();
        List<Promotion> activePromotions = promotionRepository.findByStartDateBeforeAndEndDateAfterAndActive(now, now, true);
        
        return activePromotions.stream()
                .filter(promotion -> isPromotionApplicableToProduct(promotion, product))
                .collect(Collectors.toList());
    }
    
    /**
     * 주문에 적용 가능한 활성화된 프로모션 조회
     * 
     * @param order 주문
     * @return 활성화된 프로모션 목록
     */
    @Transactional(readOnly = true)
    public List<Promotion> findActiveByOrder(Order order) {
        LocalDateTime now = LocalDateTime.now();
        List<Promotion> activePromotions = promotionRepository.findByStartDateBeforeAndEndDateAfterAndActive(now, now, true);
        
        return activePromotions.stream()
                .filter(promotion -> isPromotionApplicableToOrder(promotion, order))
                .collect(Collectors.toList());
    }
    
    /**
     * 프로모션 활성화/비활성화
     * 
     * @param id 프로모션 ID
     * @param active 활성화 여부
     * @return 업데이트된 프로모션
     */
    @Transactional
    public Optional<Promotion> updatePromotionActive(Long id, boolean active) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setActive(active);
                    return promotionRepository.save(promotion);
                });
    }
    
    /**
     * 프로모션 기간 업데이트
     * 
     * @param id 프로모션 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 업데이트된 프로모션
     */
    @Transactional
    public Optional<Promotion> updatePromotionDates(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
        }
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setStartDate(startDate);
                    promotion.setEndDate(endDate);
                    return promotionRepository.save(promotion);
                });
    }
    
    /**
     * 프로모션 할인 정보 업데이트
     * 
     * @param id 프로모션 ID
     * @param discountType 할인 유형
     * @param discountValue 할인 값
     * @return 업데이트된 프로모션
     */
    @Transactional
    public Optional<Promotion> updatePromotionDiscount(Long id, String discountType, BigDecimal discountValue) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setDiscountType(discountType);
                    promotion.setDiscountValue(discountValue);
                    return promotionRepository.save(promotion);
                });
    }
    
    /**
     * 프로모션 최소 주문 금액 업데이트
     * 
     * @param id 프로모션 ID
     * @param minimumOrderAmount 최소 주문 금액
     * @return 업데이트된 프로모션
     */
    @Transactional
    public Optional<Promotion> updateMinimumOrderAmount(Long id, BigDecimal minimumOrderAmount) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setMinimumOrderAmount(minimumOrderAmount);
                    return promotionRepository.save(promotion);
                });
    }
    
    /**
     * 프로모션 최대 사용 횟수 업데이트
     * 
     * @param id 프로모션 ID
     * @param maxUsageCount 최대 사용 횟수
     * @return 업데이트된 프로모션
     */
    @Transactional
    public Optional<Promotion> updateMaxUsageCount(Long id, Integer maxUsageCount) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setMaxUsageCount(maxUsageCount);
                    return promotionRepository.save(promotion);
                });
    }
    
    /**
     * 프로모션 코드 생성
     * 
     * @param length 코드 길이
     * @return 생성된 프로모션 코드
     */
    public String generatePromotionCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        
        String code = sb.toString();
        
        // 코드 중복 검사
        while (promotionRepository.findByCode(code).isPresent()) {
            code = generatePromotionCode(length);
        }
        
        return code;
    }
    
    /**
     * 새 프로모션 생성
     * 
     * @param name 이름
     * @param description 설명
     * @param type 유형
     * @param discountType 할인 유형
     * @param discountValue 할인 값
     * @param startDate 시작일
     * @param endDate 종료일
     * @param minimumOrderAmount 최소 주문 금액
     * @param maxUsageCount 최대 사용 횟수
     * @return 생성된 프로모션
     */
    @Transactional
    public Promotion createPromotion(String name, String description, String type, 
            String discountType, BigDecimal discountValue, LocalDateTime startDate, 
            LocalDateTime endDate, BigDecimal minimumOrderAmount, Integer maxUsageCount) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
        }
        String code = generatePromotionCode(8);
        Promotion promotion = new Promotion();
        promotion.setName(name);
        promotion.setDescription(description);
        promotion.setCode(code);
        promotion.setType(type);
        promotion.setDiscountType(discountType);
        promotion.setDiscountValue(discountValue);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setMinimumOrderAmount(minimumOrderAmount);
        promotion.setMaxUsageCount(maxUsageCount);
        promotion.setCurrentUsageCount(0);
        promotion.setActive(true);
        return promotionRepository.save(promotion);
    }
    
    /**
     * 프로모션이 사용자에게 적용 가능한지 확인
     * 
     * @param promotion 프로모션
     * @param userId 사용자 ID
     * @return 적용 가능 여부
     */
    private boolean isPromotionApplicableToUser(Promotion promotion, Long userId) {
        if (promotion.getUserIds() == null || promotion.getUserIds().isEmpty()) {
            return true;
        }
        return promotion.getUserIds().contains(userId.toString());
    }
    
    /**
     * 프로모션이 상품에 적용 가능한지 확인
     * 
     * @param promotion 프로모션
     * @param product 상품
     * @return 적용 가능 여부
     */
    private boolean isPromotionApplicableToProduct(Promotion promotion, Product product) {
        if ((promotion.getProductIds() == null || promotion.getProductIds().isEmpty()) &&
            (promotion.getCategoryIds() == null || promotion.getCategoryIds().isEmpty())) {
            return true;
        }
        if (promotion.getProductIds() != null && !promotion.getProductIds().isEmpty()) {
            if (promotion.getProductIds().contains(product.getId().toString())) {
                return true;
            }
        }
        if (promotion.getCategoryIds() != null && !promotion.getCategoryIds().isEmpty() &&
            product.getCategory() != null) {
            return promotion.getCategoryIds().contains(product.getCategory().getId().toString());
        }
        return false;
    }
    
    /**
     * 프로모션이 주문에 적용 가능한지 확인
     * 
     * @param promotion 프로모션
     * @param order 주문
     * @return 적용 가능 여부
     */
    private boolean isPromotionApplicableToOrder(Promotion promotion, Order order) {
        // getTotal(), getItems() 등은 Order에 구현되어 있어야 함
        if (promotion.getMinimumOrderAmount() != null && 
            order.getTotal().compareTo(promotion.getMinimumOrderAmount()) < 0) {
            return false;
        }
        if (!isPromotionApplicableToUser(promotion, order.getUser().getId())) {
            return false;
        }
        if ((promotion.getProductIds() != null && !promotion.getProductIds().isEmpty()) ||
            (promotion.getCategoryIds() != null && !promotion.getCategoryIds().isEmpty())) {
            return order.getItems().stream()
                    .anyMatch(item -> isPromotionApplicableToProduct(promotion, item.getProduct()));
        }
        return true;
    }
    
    /**
     * 주문에 적용될 할인 금액 계산
     * 
     * @param promotion 프로모션
     * @param order 주문
     * @return 할인 금액
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateDiscount(Promotion promotion, Order order) {
        if (!isPromotionApplicableToOrder(promotion, order)) {
            return BigDecimal.ZERO;
        }
        BigDecimal discountAmount;
        switch (promotion.getDiscountType()) {
            case "PERCENT":
                discountAmount = order.getTotal().multiply(promotion.getDiscountValue().divide(new BigDecimal(100)));
                break;
            case "FIXED":
                discountAmount = promotion.getDiscountValue();
                if (discountAmount.compareTo(order.getTotal()) > 0) {
                    discountAmount = order.getTotal();
                }
                break;
            case "FREE_SHIPPING":
                discountAmount = order.getShippingFee();
                break;
            default:
                discountAmount = BigDecimal.ZERO;
        }
        return discountAmount;
    }
    
    /**
     * 프로모션 사용 횟수 증가
     * 
     * @param id 프로모션 ID
     * @return 업데이트된 프로모션
     */
    @Transactional
    public Optional<Promotion> incrementUsageCount(Long id) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    Integer currentCount = promotion.getCurrentUsageCount();
                    if (currentCount == null) {
                        currentCount = 0;
                    }
                    if (promotion.getMaxUsageCount() != null && 
                        currentCount >= promotion.getMaxUsageCount()) {
                        throw new IllegalStateException("프로모션 최대 사용 횟수를 초과했습니다.");
                    }
                    promotion.setCurrentUsageCount(currentCount + 1);
                    if (promotion.getMaxUsageCount() != null && 
                        promotion.getCurrentUsageCount() >= promotion.getMaxUsageCount()) {
                        promotion.setActive(false);
                    }
                    return promotionRepository.save(promotion);
                });
    }
    
    /**
     * 사용자별 프로모션 페이징 조회
     * 
     * @param user 사용자
     * @param pageable 페이징 정보
     * @return 프로모션 페이지
     */
    @Transactional(readOnly = true)
    public Page<Promotion> findAllActiveForUser(User user, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = user.getId();
        Page<Promotion> activePromotions = promotionRepository.findByStartDateBeforeAndEndDateAfterAndActive(
                now, now, true, pageable);
        List<Promotion> filtered = activePromotions.getContent().stream()
                .filter(promotion -> isPromotionApplicableToUser(promotion, userId))
                .toList();
        return new org.springframework.data.domain.PageImpl<>(filtered, pageable, filtered.size());
    }
}