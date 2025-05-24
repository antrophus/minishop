package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recurring_orders", indexes = {
    @Index(name = "idx_recurring_order_user_id", columnList = "user_id"),
    @Index(name = "idx_recurring_order_subscription_id", columnList = "subscription_id"),
    @Index(name = "idx_recurring_order_status", columnList = "status"),
    @Index(name = "idx_recurring_order_next_order_date", columnList = "next_order_date")
})
@Getter @Setter
@NoArgsConstructor
public class RecurringOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
    
    @OneToMany(mappedBy = "recurringOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurringOrderItem> items = new ArrayList<>();
    
    @OneToMany(mappedBy = "recurringOrder")
    private List<Order> orders = new ArrayList<>();
    
    @Column(length = 20)
    private String status = "ACTIVE";
    
    @Column(length = 20, nullable = false)
    private String frequency;
    
    @Column(name = "next_order_date", nullable = false)
    private LocalDate nextOrderDate;
    
    @Column(name = "last_order_date")
    private LocalDate lastOrderDate;
    
    @Column(name = "shipping_address_id")
    private Long shippingAddressId;
    
    @Column(name = "payment_method_id")
    private Long paymentMethodId;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 편의 메서드: 아이템 추가
    public void addItem(RecurringOrderItem item) {
        items.add(item);
        item.setRecurringOrder(this);
    }
    
    // 편의 메서드: 아이템 제거
    public void removeItem(RecurringOrderItem item) {
        items.remove(item);
        item.setRecurringOrder(null);
    }
    
    // 편의 메서드: 주문 생성후 다음 주문일 계산
    public void calculateNextOrderDate() {
        if (lastOrderDate == null) {
            lastOrderDate = LocalDate.now();
        }
        
        switch (frequency.toUpperCase()) {
            case "DAILY":
                nextOrderDate = lastOrderDate.plusDays(1);
                break;
            case "WEEKLY":
                nextOrderDate = lastOrderDate.plusWeeks(1);
                break;
            case "BIWEEKLY":
                nextOrderDate = lastOrderDate.plusWeeks(2);
                break;
            case "MONTHLY":
                nextOrderDate = lastOrderDate.plusMonths(1);
                break;
            case "QUARTERLY":
                nextOrderDate = lastOrderDate.plusMonths(3);
                break;
            case "BIANNUALLY":
                nextOrderDate = lastOrderDate.plusMonths(6);
                break;
            case "ANNUALLY":
                nextOrderDate = lastOrderDate.plusYears(1);
                break;
            default:
                // 기본값: 한 달
                nextOrderDate = lastOrderDate.plusMonths(1);
        }
    }
}