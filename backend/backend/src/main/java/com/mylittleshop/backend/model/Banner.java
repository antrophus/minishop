package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "banner")
@Getter @Setter
@NoArgsConstructor
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 255)
    private String title;
    
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;
    
    @Column(name = "link_url", length = 500)
    private String linkUrl;
    
    @Column(length = 50)
    private String position;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 편의 메서드: 배너 활성화 여부 확인
    public boolean isActive() {
        if (!isActive) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        boolean afterStartDate = startDate == null || !now.isBefore(startDate);
        boolean beforeEndDate = endDate == null || !now.isAfter(endDate);
        
        return afterStartDate && beforeEndDate;
    }
}