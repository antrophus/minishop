package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_media")
@Getter @Setter
@NoArgsConstructor
public class ProductMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, length = 500)
    private String mediaUrl;

    @Column(nullable = false, length = 20)
    private String mediaType; // IMAGE, VIDEO 등

    @Column(nullable = false)
    private Boolean isAIGenerated = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
} 