package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription_plans")
@Getter @Setter
@NoArgsConstructor
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer duration; // 기간(예: 개월 수)

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean isActive = true;
} 