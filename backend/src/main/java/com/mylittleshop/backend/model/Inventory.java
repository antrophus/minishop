package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter @Setter
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 편의 메서드: 재고 입고
    public void addStock(int quantity) {
        this.quantity += quantity;
        this.availableStock += quantity;
    }
    
    // 편의 메서드: 재고 출고
    public void removeStock(int quantity) {
        if (this.availableStock < quantity) {
            throw new IllegalArgumentException("사용 가능한 재고가 부족합니다.");
        }
        
        this.availableStock -= quantity;
    }
    
    // 편의 메서드: 재고 조정
    public void adjustStock(int newQuantity) {
        int diff = newQuantity - this.quantity;
        this.quantity = newQuantity;
        
        if (diff > 0) {
            // 재고 증가
            this.availableStock += diff;
        } else if (diff < 0 && this.availableStock + diff >= 0) {
            // 재고 감소
            this.availableStock += diff;
        } else {
            // 가용 재고보다 더 많이 감소시키려는 경우
            throw new IllegalArgumentException("가용 재고보다 더 많은 수량을 감소시킬 수 없습니다.");
        }
    }
    
    // 편의 메서드: 재고 예약
    public void reserveStock(int quantity) {
        if (this.availableStock < quantity) {
            throw new IllegalArgumentException("사용 가능한 재고가 부족합니다.");
        }
        
        this.availableStock -= quantity;
    }
    
    // 편의 메서드: 재고 예약 취소
    public void cancelReservation(int quantity) {
        this.availableStock += quantity;
        
        if (this.availableStock > this.quantity) {
            this.availableStock = this.quantity;
        }
    }
}