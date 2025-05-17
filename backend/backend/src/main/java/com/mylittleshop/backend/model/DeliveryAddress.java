package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_addresses")
@Getter @Setter
@NoArgsConstructor
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 255)
    private String recipientName;

    @Column(nullable = false, length = 15)
    private String contactNumber;

    @Column(nullable = false, length = 500)
    private String address;
} 