package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_media", indexes = {
    @Index(name = "idx_product_media_product", columnList = "product_id"),
    @Index(name = "idx_product_media_type", columnList = "media_type"),
    @Index(name = "idx_product_media_featured", columnList = "is_featured")
})
@Getter @Setter
@NoArgsConstructor
public class ProductMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 500)
    private String mediaUrl;
    
    @Column(length = 255)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false, length = 20)
    private MediaType mediaType = MediaType.IMAGE;
    
    @Column(length = 255)
    private String altText;
    
    @Column(length = 255)
    private String title;
    
    @Column(nullable = false)
    private Integer displayOrder = 0;
    
    @Column(nullable = false, name = "is_featured")
    private Boolean isFeatured = false;

    @Column(nullable = false)
    private Boolean isAIGenerated = false;
    
    @Column(length = 255)
    private String aiPromptUsed;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(length = 20)
    private String dimensions;
    
    @Column
    private Long fileSize;
    
    @Column(length = 100)
    private String fileType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // 미디어 유형 열거형
    public enum MediaType {
        IMAGE,      // 이미지
        VIDEO,      // 비디오
        DOCUMENT,   // 문서
        MODEL_3D,   // 3D 모델
        AUDIO       // 오디오
    }
} 