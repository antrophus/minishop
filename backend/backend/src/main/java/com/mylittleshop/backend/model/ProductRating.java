package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_ratings", indexes = {
    @Index(name = "idx_rating_product", columnList = "product_id"),
    @Index(name = "idx_rating_average", columnList = "average_rating")
})
@Getter @Setter
@NoArgsConstructor
public class ProductRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
    
    @Column(name = "average_rating", nullable = false, precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private Integer ratingCount = 0;
    
    // 별점 분포 (1~5점까지의 각 별점 개수)
    @Column(nullable = false)
    private Integer oneStarCount = 0;
    
    @Column(nullable = false)
    private Integer twoStarCount = 0;
    
    @Column(nullable = false)
    private Integer threeStarCount = 0;
    
    @Column(nullable = false)
    private Integer fourStarCount = 0;
    
    @Column(nullable = false)
    private Integer fiveStarCount = 0;
    
    // 검증된 구매에 의한 리뷰 수
    @Column(nullable = false)
    private Integer verifiedPurchaseCount = 0;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // 편의 메서드: 새 평점 추가
    public void addRating(int rating, boolean isVerifiedPurchase) {
        // 평점 수 증가
        this.ratingCount++;
        
        // 검증된 구매 카운트 증가
        if (isVerifiedPurchase) {
            this.verifiedPurchaseCount++;
        }
        
        // 평점 분포 업데이트
        switch (rating) {
            case 1 -> this.oneStarCount++;
            case 2 -> this.twoStarCount++;
            case 3 -> this.threeStarCount++;
            case 4 -> this.fourStarCount++;
            case 5 -> this.fiveStarCount++;
            default -> throw new IllegalArgumentException("유효하지 않은 평점입니다: " + rating);
        }
        
        // 평균 평점 재계산
        recalculateAverageRating();
    }
    
    // 편의 메서드: 평점 삭제
    public void removeRating(int rating, boolean isVerifiedPurchase) {
        if (this.ratingCount <= 0) {
            return;
        }
        
        // 평점 수 감소
        this.ratingCount--;
        
        // 검증된 구매 카운트 감소
        if (isVerifiedPurchase && this.verifiedPurchaseCount > 0) {
            this.verifiedPurchaseCount--;
        }
        
        // 평점 분포 업데이트
        switch (rating) {
            case 1 -> this.oneStarCount = Math.max(0, this.oneStarCount - 1);
            case 2 -> this.twoStarCount = Math.max(0, this.twoStarCount - 1);
            case 3 -> this.threeStarCount = Math.max(0, this.threeStarCount - 1);
            case 4 -> this.fourStarCount = Math.max(0, this.fourStarCount - 1);
            case 5 -> this.fiveStarCount = Math.max(0, this.fiveStarCount - 1);
            default -> throw new IllegalArgumentException("유효하지 않은 평점입니다: " + rating);
        }
        
        // 평균 평점 재계산
        recalculateAverageRating();
    }
    
    // 내부 메서드: 평균 평점 재계산
    private void recalculateAverageRating() {
        if (this.ratingCount <= 0) {
            this.averageRating = BigDecimal.ZERO;
            return;
        }
        
        int sum = this.oneStarCount * 1 +
                  this.twoStarCount * 2 +
                  this.threeStarCount * 3 +
                  this.fourStarCount * 4 +
                  this.fiveStarCount * 5;
                  
        this.averageRating = BigDecimal.valueOf((double) sum / this.ratingCount);
    }
} 