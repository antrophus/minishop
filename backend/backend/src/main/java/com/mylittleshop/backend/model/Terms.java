package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "terms")
@Getter @Setter
@NoArgsConstructor
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false, length = 20)
    private String version;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 편의 메서드: 버전 비교
    public boolean isNewerThan(Terms other) {
        if (other == null) {
            return true;
        }
        
        // 버전 형식이 "1.0.0" 형태라고 가정
        String[] thisVersion = this.version.split("\\.");
        String[] otherVersion = other.version.split("\\.");
        
        for (int i = 0; i < Math.min(thisVersion.length, otherVersion.length); i++) {
            int thisPart = Integer.parseInt(thisVersion[i]);
            int otherPart = Integer.parseInt(otherVersion[i]);
            
            if (thisPart > otherPart) {
                return true;
            } else if (thisPart < otherPart) {
                return false;
            }
        }
        
        // 여기까지 왔다면 공통 부분이 모두 같음
        return thisVersion.length > otherVersion.length;
    }
}