package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscription_plans", indexes = {
    @Index(name = "idx_subscription_plan_is_active", columnList = "is_active")
})
@Getter @Setter
@NoArgsConstructor
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer duration;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_frequency", length = 20)
    private BillingFrequency billingFrequency = BillingFrequency.MONTHLY;
    
    @Column(length = 255)
    private String description;
    
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    @Column(name = "trial_period_days")
    private Integer trialPeriodDays = 0;
    
    @Column(name = "features", columnDefinition = "JSON")
    private String features;
    
    @Column(name = "max_pause_count")
    private Integer maxPauseCount = 3;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "includes_recurring_order")
    private Boolean includesRecurringOrder = false;
    
    @OneToMany(mappedBy = "plan")
    private List<Subscription> subscriptions = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}