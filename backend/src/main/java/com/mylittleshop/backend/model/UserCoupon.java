package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupons")
@Getter @Setter
@NoArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
    
    @Column(length = 20)
    private String status = "AVAILABLE"; // AVAILABLE, USED, EXPIRED
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 편의 메서드: 쿠폰 사용
    public void use() {
        this.status = "USED";
        this.usedAt = LocalDateTime.now();
    }
    
    // 편의 메서드: 쿠폰 만료
    public void expire() {
        this.status = "EXPIRED";
    }
    
    // 편의 메서드: 사용 가능 여부 확인
    public boolean isAvailable() {
        return "AVAILABLE".equals(status) && coupon.isValid();
    }
}