package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_payments", indexes = {
    @Index(name = "idx_subscription_payment_subscription_id", columnList = "subscription_id"),
    @Index(name = "idx_subscription_payment_status", columnList = "status"),
    @Index(name = "idx_subscription_payment_transaction_id", columnList = "transaction_id")
})
@Getter @Setter
@NoArgsConstructor
public class SubscriptionPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "original_amount", precision = 12, scale = 2)
    private BigDecimal originalAmount;
    
    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @CreationTimestamp
    @Column(name = "payment_date", nullable = false, updatable = false)
    private LocalDateTime paymentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus status;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "payment_method_id")
    private Long paymentMethodId;
    
    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse;
    
    @Column(name = "billing_period_start")
    private LocalDate billingPeriodStart;
    
    @Column(name = "billing_period_end")
    private LocalDate billingPeriodEnd;
    
    @Column(name = "invoice_id", length = 100)
    private String invoiceId;
    
    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;
    
    @Column(name = "retry_count")
    private Integer retryCount = 0;
    
    @Column(name = "next_retry_date")
    private LocalDateTime nextRetryDate;
    
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}