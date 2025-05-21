package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurring_order_items", indexes = {
    @Index(name = "idx_recurring_order_item_recurring_order_id", columnList = "recurring_order_id"),
    @Index(name = "idx_recurring_order_item_product_id", columnList = "product_id")
})
@Getter @Setter
@NoArgsConstructor
public class RecurringOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurring_order_id", nullable = false)
    private RecurringOrder recurringOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false)
    private Integer quantity = 1;
    
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "applied_price_type", length = 20)
    private String appliedPriceType = "REGULAR";
    
    @Column(name = "is_gift")
    private Boolean isGift = false;
    
    @Column(name = "gift_message", columnDefinition = "TEXT")
    private String giftMessage;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}