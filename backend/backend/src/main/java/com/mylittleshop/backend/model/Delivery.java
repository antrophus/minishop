package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deliveries")
@Getter @Setter
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;
    
    @Column(name = "contact_number", nullable = false, length = 20)
    private String contactNumber;
    
    @Column(nullable = false, length = 255)
    private String address;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DeliveryStatus status;
}