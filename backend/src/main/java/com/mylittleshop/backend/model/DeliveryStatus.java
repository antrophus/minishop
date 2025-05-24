package com.mylittleshop.backend.model;

public enum DeliveryStatus {
    PENDING,
    PROCESSING,
    READY_FOR_PICKUP,
    IN_TRANSIT,
    DELIVERED,
    FAILED,
    RETURNED,
    CANCELLED
}