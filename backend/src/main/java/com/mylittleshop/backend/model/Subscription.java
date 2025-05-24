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
@Table(name = "subscriptions", indexes = {
    @Index(name = "idx_subscription_user_id", columnList = "user_id"),
    @Index(name = "idx_subscription_product_id", columnList = "product_id"),
    @Index(name = "idx_subscription_plan_id", columnList = "plan_id"),
    @Index(name = "idx_subscription_status", columnList = "status"),
    @Index(name = "idx_subscription_next_payment_date", columnList = "next_payment_date"),
    @Index(name = "idx_subscription_contract_end_date", columnList = "contract_end_date")
})
@Getter @Setter
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;
    
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<SubscriptionPayment> payments = new ArrayList<>();
    
    @OneToMany(mappedBy = "subscription")
    private List<RecurringOrder> recurringOrders = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;
    
    @Column(length = 30, nullable = false)
    private String frequency;
    
    @Column(name = "next_payment_date", nullable = false)
    private LocalDateTime nextPaymentDate;

    @Column(name = "next_billing_date")
    private LocalDate nextBillingDate;
    
    @Column(name = "next_delivery_date")
    private LocalDateTime nextDeliveryDate;
    
    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;
    
    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;
    
    @Column(name = "trial_end_date")
    private LocalDate trialEndDate;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "customer_profile_id", length = 100)
    private String customerProfileId;
    
    @Column(name = "auto_renew")
    private Boolean autoRenew = true;
    
    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;
    
    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;
    
    @Column(name = "pause_start_date")
    private LocalDateTime pauseStartDate;
    
    @Column(name = "pause_end_date")
    private LocalDateTime pauseEndDate;
    
    @Column(name = "pause_reason", columnDefinition = "TEXT")
    private String pauseReason;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 편의 메서드: 구독 상태 변경
    public void changeStatus(SubscriptionStatus newStatus) {
        this.status = newStatus;
    }
    
    // 편의 메서드: 구독 취소
    public void cancel(String reason) {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancellationDate = LocalDateTime.now();
        this.cancellationReason = reason;
        this.autoRenew = false;
    }
    
    // 편의 메서드: 구독 일시 중지
    public void pause(LocalDateTime endDate, String reason) {
        this.status = SubscriptionStatus.PAUSED;
        this.pauseStartDate = LocalDateTime.now();
        this.pauseEndDate = endDate;
        this.pauseReason = reason;
    }
    
    // 편의 메서드: 구독 재개
    public void resume() {
        this.status = SubscriptionStatus.ACTIVE;
        this.pauseEndDate = LocalDateTime.now();
    }
    
    // 편의 메서드: 결제 추가
    public void addPayment(SubscriptionPayment payment) {
        payments.add(payment);
        payment.setSubscription(this);
    }
    
    // 편의 메서드: 다음 결제일 계산
    public void calculateNextPaymentDate() {
        LocalDateTime now = LocalDateTime.now();
    
        if (frequency.equalsIgnoreCase("DAILY")) {
            this.nextPaymentDate = now.plusDays(1);
            this.nextBillingDate = now.plusDays(1).toLocalDate();
        } else if (frequency.equalsIgnoreCase("WEEKLY")) {
            this.nextPaymentDate = now.plusWeeks(1);
            this.nextBillingDate = now.plusWeeks(1).toLocalDate();
        } else if (frequency.equalsIgnoreCase("BIWEEKLY")) {
            this.nextPaymentDate = now.plusWeeks(2);
            this.nextBillingDate = now.plusWeeks(2).toLocalDate();
        } else if (frequency.equalsIgnoreCase("MONTHLY")) {
            this.nextPaymentDate = now.plusMonths(1);
            this.nextBillingDate = now.plusMonths(1).toLocalDate();
        } else if (frequency.equalsIgnoreCase("QUARTERLY")) {
            this.nextPaymentDate = now.plusMonths(3);
            this.nextBillingDate = now.plusMonths(3).toLocalDate();
        } else if (frequency.equalsIgnoreCase("BIANNUALLY")) {
            this.nextPaymentDate = now.plusMonths(6);
            this.nextBillingDate = now.plusMonths(6).toLocalDate();
        } else if (frequency.equalsIgnoreCase("ANNUALLY")) {
            this.nextPaymentDate = now.plusYears(1);
            this.nextBillingDate = now.plusYears(1).toLocalDate();
        }
    }
}