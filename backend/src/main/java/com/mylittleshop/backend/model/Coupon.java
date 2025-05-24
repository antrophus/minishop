package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
@Getter @Setter
@NoArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 50, nullable = false, unique = true)
    private String code;
    
    @Column(length = 255)
    private String description;
    
    @Column(name = "discount_type", length = 20, nullable = false)
    private String discountType; // PERCENT, FIXED
    
    @Column(name = "discount_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountValue;
    
    @Column(name = "min_order_amount")
    private Integer minOrderAmount;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons = new ArrayList<>();
    
    // 편의 메서드: 쿠폰 활성화 여부 확인
    public boolean isValid() {
        if (!isActive) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        boolean afterStartDate = startDate == null || !now.isBefore(startDate);
        boolean beforeEndDate = endDate == null || !now.isAfter(endDate);
        
        return afterStartDate && beforeEndDate;
    }
    
    // 편의 메서드: 할인 금액 계산
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!isValid()) {
            return BigDecimal.ZERO;
        }
        
        // 최소 주문 금액 체크
        if (minOrderAmount != null && orderAmount.compareTo(BigDecimal.valueOf(minOrderAmount)) < 0) {
            return BigDecimal.ZERO;
        }
        
        if ("PERCENT".equals(discountType)) {
            // 퍼센트 할인
            return orderAmount.multiply(discountValue.divide(BigDecimal.valueOf(100)));
        } else if ("FIXED".equals(discountType)) {
            // 정액 할인
            return discountValue.min(orderAmount); // 주문 금액보다 크면 주문 금액만큼만 할인
        }
        
        return BigDecimal.ZERO;
    }
    
    // 편의 메서드: 사용자에게 쿠폰 할당
    public UserCoupon assignToUser(User user) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUser(user);
        userCoupon.setCoupon(this);
        userCoupon.setStatus("AVAILABLE");
        
        userCoupons.add(userCoupon);
        return userCoupon;
    }
}