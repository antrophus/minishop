package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_reviews", indexes = {
    @Index(name = "idx_review_product", columnList = "product_id"),
    @Index(name = "idx_review_user", columnList = "user_id"),
    @Index(name = "idx_review_status", columnList = "status"),
    @Index(name = "idx_review_rating", columnList = "rating")
})
@Getter @Setter
@NoArgsConstructor
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(length = 255)
    private String title;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private Integer helpfulVotes = 0;
    
    @Column(nullable = false)
    private Integer unhelpfulVotes = 0;
    
    @Column(nullable = false)
    private Boolean verifiedPurchase = false;
    
    @OneToMany(mappedBy = "parentReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewResponse> responses = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ReviewStatus status = ReviewStatus.PENDING;
    
    @Column(length = 500)
    private String moderationNotes;
    
    @Column(length = 100)
    private String moderatedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime approvedAt;
    
    // 편의 메서드: 도움이 되었다는 투표 추가
    public void addHelpfulVote() {
        this.helpfulVotes++;
    }
    
    // 편의 메서드: 도움이 되지 않았다는 투표 추가
    public void addUnhelpfulVote() {
        this.unhelpfulVotes++;
    }
    
    // 편의 메서드: 리뷰 승인
    public void approve(String moderatedBy) {
        this.status = ReviewStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        this.moderatedBy = moderatedBy;
    }
    
    // 편의 메서드: 리뷰 거부
    public void reject(String moderatedBy, String notes) {
        this.status = ReviewStatus.REJECTED;
        this.moderatedBy = moderatedBy;
        this.moderationNotes = notes;
    }
    
    // 편의 메서드: 리뷰 신고
    public void flag(String notes) {
        this.status = ReviewStatus.FLAGGED;
        this.moderationNotes = notes;
    }
    
    // 편의 메서드: 응답 추가
    public void addResponse(ReviewResponse response) {
        this.responses.add(response);
        response.setParentReview(this);
    }
} 