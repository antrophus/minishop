package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @Setter
public class Delivery {
    @Column(nullable = false, length = 255)
    private String recipientName;

    @Column(nullable = false, length = 15)
    private String contactNumber;

    @Column(nullable = false, length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
} 