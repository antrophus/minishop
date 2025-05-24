package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_responses", indexes = {
    @Index(name = "idx_response_review", columnList = "review_id"),
    @Index(name = "idx_response_user", columnList = "user_id")
})
@Getter @Setter
@NoArgsConstructor
public class ReviewResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ProductReview parentReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private Boolean isSellerResponse = false;
    
    @Column(nullable = false)
    private Boolean isAdminResponse = false;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private Integer helpfulVotes = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // 편의 메서드: 도움이 되었다는 투표 추가
    public void addHelpfulVote() {
        this.helpfulVotes++;
    }
} 