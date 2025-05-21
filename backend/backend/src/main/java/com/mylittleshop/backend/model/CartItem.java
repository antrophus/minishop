package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_item_cart_id", columnList = "cart_id"),
    @Index(name = "idx_cart_item_product_id", columnList = "product_id")
})
@Getter @Setter
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "applied_price_type", length = 20)
    private String appliedPriceType = "REGULAR";
    
    @Column(name = "is_gift")
    private Boolean isGift = false;
    
    @Column(name = "gift_message", columnDefinition = "TEXT")
    private String giftMessage;
    
    @Column(name = "product_options", columnDefinition = "JSON")
    private String productOptions;
    
    @Column(name = "selected_attributes", columnDefinition = "JSON")
    private String selectedAttributes;
    
    @CreationTimestamp
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 편의 메서드: 수량 증가
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }
    
    // 편의 메서드: 수량 감소
    public void decreaseQuantity(int amount) {
        if (this.quantity - amount <= 0) {
            this.quantity = 0;
        } else {
            this.quantity -= amount;
        }
    }
    
    // 편의 메서드: 총 금액 계산
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}