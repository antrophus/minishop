package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_images")
@Getter @Setter
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column(name = "is_main")
    private Boolean isMain = false;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // alt 필드 제거 (DB 스키마에 없음)
    
    public ProductImage(String url) {
        this.url = url;
    }
    
    public ProductImage(String url, boolean isMain) {
        this.url = url;
        this.isMain = isMain;
    }
    
    public ProductImage(String url, boolean isMain, int sortOrder) {
        this.url = url;
        this.isMain = isMain;
        this.sortOrder = sortOrder;
    }
}