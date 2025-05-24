package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_order_id", columnList = "order_id"),
    @Index(name = "idx_payment_status", columnList = "status"),
    @Index(name = "idx_payment_transaction_id", columnList = "transaction_id")
})
@Getter @Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    
    @CreationTimestamp
    @Column(name = "payment_date", nullable = false, updatable = false)
    private LocalDateTime paymentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus status;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse;
    
    @Column(name = "payment_attempt_timestamp")
    private LocalDateTime paymentAttemptTimestamp;
    
    @Column(name = "payment_completion_timestamp")
    private LocalDateTime paymentCompletionTimestamp;
    
    @Column(name = "payment_failed_timestamp")
    private LocalDateTime paymentFailedTimestamp;
    
    @Column(name = "payment_cancelled_timestamp")
    private LocalDateTime paymentCancelledTimestamp;
    
    @Column(name = "payment_metadata", columnDefinition = "JSON")
    private String paymentMetadata;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
}