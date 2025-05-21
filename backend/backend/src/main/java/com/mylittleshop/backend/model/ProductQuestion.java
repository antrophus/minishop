package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_questions")
@Getter @Setter
@NoArgsConstructor
public class ProductQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;
    
    @Column(columnDefinition = "TEXT")
    private String answer;
    
    @Column(length = 20)
    private String status = "PENDING"; // PENDING, ANSWERED, REJECTED
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
    
    // 편의 메서드: 질문 답변
    public void answer(String answer) {
        this.answer = answer;
        this.status = "ANSWERED";
        this.answeredAt = LocalDateTime.now();
    }
    
    // 편의 메서드: 질문 거부
    public void reject() {
        this.status = "REJECTED";
        this.answeredAt = LocalDateTime.now();
    }
    
    // 편의 메서드: 답변 여부 확인
    public boolean isAnswered() {
        return "ANSWERED".equals(status);
    }
    
    // 편의 메서드: 거부 여부 확인
    public boolean isRejected() {
        return "REJECTED".equals(status);
    }
}