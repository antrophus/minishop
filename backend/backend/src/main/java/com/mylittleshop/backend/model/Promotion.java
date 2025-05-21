package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "promotions")
@Getter @Setter
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 255)
    private String description;
    
    @Column(unique = true, length = 32)
    private String code;
    
    @Column(length = 30)
    private String type;
    
    @Column(length = 30)
    private String discountType;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal discountValue;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal minimumOrderAmount;
    
    private Integer maxUsageCount;
    private Integer currentUsageCount;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @ElementCollection
    @CollectionTable(name = "promotion_product_ids", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "product_id")
    private List<String> productIds;
    
    @ElementCollection
    @CollectionTable(name = "promotion_category_ids", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "category_id")
    private List<String> categoryIds;
    
    @ElementCollection
    @CollectionTable(name = "promotion_user_ids", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "user_id")
    private List<String> userIds;
    
    @ManyToMany(mappedBy = "promotions")
    private Set<Product> products = new HashSet<>();
    
    public Promotion(String name, BigDecimal discountRate) {
        this.name = name;
        this.discountValue = discountRate;
    }
    
    // 편의 메서드: 프로모션 활성화 여부
    public boolean isActiveNow() {
        LocalDateTime now = LocalDateTime.now();
        
        boolean afterStart = startDate == null || !now.isBefore(startDate);
        boolean beforeEnd = endDate == null || !now.isAfter(endDate);
        
        return active && afterStart && beforeEnd;
    }
}