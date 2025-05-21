package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_for_later", indexes = {
    @Index(name = "idx_saved_for_later_user_id", columnList = "user_id"),
    @Index(name = "idx_saved_for_later_product_id", columnList = "product_id")
})
@Getter @Setter
@NoArgsConstructor
public class SavedForLater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(length = 500, name = "user_notes")
    private String userNotes;
    
    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;
    
    @Column(name = "reminder_sent", nullable = false)
    private Boolean reminderSent = false;
    
    @Column(name = "priority", length = 20)
    private String priority;
    
    @Column(name = "source", length = 50)
    private String source;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 편의 메서드: 알림 설정
    public void setReminder(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
        this.reminderSent = false;
    }
    
    // 편의 메서드: 알림 전송 처리
    public void markReminderSent() {
        this.reminderSent = true;
    }
    
    // 편의 메서드: 노트 추가
    public void addNote(String note) {
        this.userNotes = note;
    }
    
    // 편의 메서드: 우선순위 설정
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    // 편의 메서드: 알림 필요 여부 체크
    public boolean isReminderNeeded() {
        return this.reminderDate != null 
                && !this.reminderSent 
                && LocalDateTime.now().isAfter(this.reminderDate);
    }
} 