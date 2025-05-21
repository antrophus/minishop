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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;
    
    @Column(name = "contact_number", nullable = false, length = 20)
    private String contactNumber;
    
    @Column(nullable = false, length = 255)
    private String address;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
}