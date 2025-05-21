package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_orders_user_id", columnList = "user_id"),
    @Index(name = "idx_orders_status", columnList = "status"),
    @Index(name = "idx_orders_order_number", columnList = "order_number"),
    @Index(name = "idx_orders_recurring_order_id", columnList = "recurring_order_id")
})
@Getter @Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, updatable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurring_order_id")
    private RecurringOrder recurringOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shipment> shipments = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "final_amount", nullable = false, precision = 12, scale = 2) 
    private BigDecimal finalAmount = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "shipping_method", length = 50)
    private String shippingMethod;
    
    @Column(name = "shipping_fee", precision = 12, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "coupon_code", length = 50)
    private String couponCode;
    
    @Column(name = "is_gift")
    private Boolean isGift = false;
    
    @Column(name = "gift_message", length = 500)
    private String giftMessage;
    
    @Column(name = "order_notes", length = 500)
    private String orderNotes;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;
    
    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring = false;
    
    @Column(name = "created_by", length = 50)
    private String createdBy;
    
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    // 편의 메서드: 주문 항목 추가
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        // 주문 금액 업데이트
        this.totalAmount = this.totalAmount.add(
            orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
        );
        this.finalAmount = this.totalAmount.subtract(this.discountAmount).add(this.shippingFee).add(this.taxAmount);
    }
    
    // 편의 메서드: 주문 항목 제거
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
        // 주문 금액 업데이트
        this.totalAmount = this.totalAmount.subtract(
            orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
        );
        this.finalAmount = this.totalAmount.subtract(this.discountAmount).add(this.shippingFee).add(this.taxAmount);
    }
    
    // 편의 메서드: 할인 적용
    public void applyDiscount(BigDecimal discount) {
        this.discountAmount = discount;
        this.finalAmount = this.totalAmount.subtract(this.discountAmount).add(this.shippingFee).add(this.taxAmount);
    }
    
    // 편의 메서드: 배송비 설정
    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
        this.finalAmount = this.totalAmount.subtract(this.discountAmount).add(this.shippingFee).add(this.taxAmount);
    }
    
    // 편의 메서드: 세금 설정
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        this.finalAmount = this.totalAmount.subtract(this.discountAmount).add(this.shippingFee).add(this.taxAmount);
    }
    
    // 편의 메서드: 결제 추가
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setOrder(this);
    }
    
    // 편의 메서드: 결제 제거
    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setOrder(null);
    }
    
    // 편의 메서드: 배송 추가
    public void addShipment(Shipment shipment) {
        shipments.add(shipment);
        shipment.setOrder(this);
    }
    
    // 편의 메서드: 배송 제거
    public void removeShipment(Shipment shipment) {
        shipments.remove(shipment);
        shipment.setOrder(null);
    }
    
    // 주문 상태 변경 메서드
    public void changeStatus(OrderStatus newStatus, String reason, String changedBy) {
        // 이전 상태 저장
        OrderStatus previousStatus = this.status;
        
        // 상태 변경
        this.status = newStatus;
        
        // 상태 변경 이력 기록
        OrderStatusHistory historyEntry = new OrderStatusHistory();
        historyEntry.setOrder(this);
        historyEntry.setPreviousStatus(previousStatus);
        historyEntry.setNewStatus(newStatus);
        historyEntry.setChangeReason(reason);
        historyEntry.setChangedBy(changedBy);
        historyEntry.setChangedAt(LocalDateTime.now());
        
        this.statusHistory.add(historyEntry);
        this.updatedBy = changedBy;
    }
    
    // 총 결제 금액 계산
    public BigDecimal getTotalPaidAmount() {
        return payments.stream()
            .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // 미결제 금액 계산
    public BigDecimal getRemainingAmount() {
        return finalAmount.subtract(getTotalPaidAmount());
    }
    
    // 결제 완료 여부 확인
    public boolean isFullyPaid() {
        return getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0;
    }
    
    // 주문번호 생성 메서드
    @PrePersist
    public void generateOrderNumber() {
        if (this.orderNumber == null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String random = String.valueOf((int)(Math.random() * 1000));
            this.orderNumber = "ORD-" + timestamp.substring(timestamp.length() - 8) + "-" + random;
        }
    }

    // 프로모션/할인 계산용: 주문 총액 반환
    public BigDecimal getTotal() {
        return this.totalAmount;
    }

    // 프로모션/할인 계산용: 주문 아이템 리스트 반환
    public List<OrderItem> getItems() {
        return this.orderItems;
    }
}