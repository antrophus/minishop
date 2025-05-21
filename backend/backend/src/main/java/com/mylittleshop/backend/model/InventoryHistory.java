package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_history")
@Getter @Setter
@NoArgsConstructor
public class InventoryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "change_type", nullable = false, length = 30)
    private String changeType; // IN, OUT, ADJUST
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
    
    @Column(length = 255)
    private String note;
    
    // 편의 메서드: 입고 기록 생성
    public static InventoryHistory createInbound(Product product, int quantity, String note) {
        InventoryHistory history = new InventoryHistory();
        history.setProduct(product);
        history.setChangeType("IN");
        history.setQuantity(quantity);
        history.setChangedAt(LocalDateTime.now());
        history.setNote(note);
        return history;
    }
    
    // 편의 메서드: 출고 기록 생성
    public static InventoryHistory createOutbound(Product product, int quantity, String note) {
        InventoryHistory history = new InventoryHistory();
        history.setProduct(product);
        history.setChangeType("OUT");
        history.setQuantity(quantity);
        history.setChangedAt(LocalDateTime.now());
        history.setNote(note);
        return history;
    }
    
    // 편의 메서드: 조정 기록 생성
    public static InventoryHistory createAdjustment(Product product, int quantity, String note) {
        InventoryHistory history = new InventoryHistory();
        history.setProduct(product);
        history.setChangeType("ADJUST");
        history.setQuantity(quantity);
        history.setChangedAt(LocalDateTime.now());
        history.setNote(note);
        return history;
    }
}