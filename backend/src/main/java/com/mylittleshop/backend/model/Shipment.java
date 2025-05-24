package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments", indexes = {
    @Index(name = "idx_shipment_order_id", columnList = "order_id"),
    @Index(name = "idx_shipment_tracking_number", columnList = "tracking_number"),
    @Index(name = "idx_shipment_status", columnList = "status")
})
@Getter @Setter
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ShipmentStatus status;
    
    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;
    
    @Column(length = 50)
    private String carrier;
    
    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDate actualDeliveryDate;
    
    @Column(name = "recipient_name", length = 100)
    private String recipientName;
    
    @Column(name = "recipient_phone", length = 20)
    private String recipientPhone;
    
    @Column(name = "address_line1", length = 255)
    private String addressLine1;
    
    @Column(name = "address_line2", length = 255)
    private String addressLine2;
    
    @Column(length = 100)
    private String city;
    
    @Column(length = 100)
    private String state;
    
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    @Column(length = 50)
    private String country;
    
    @Column(name = "delivery_instructions", columnDefinition = "TEXT")
    private String deliveryInstructions;
}