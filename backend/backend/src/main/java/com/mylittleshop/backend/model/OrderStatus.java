package com.mylittleshop.backend.model;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    REFUNDED,
    PAYMENT_PENDING,
    PAYMENT_FAILED,
    ON_HOLD,
    RETURNED,
    PARTIALLY_SHIPPED,
    PARTIALLY_DELIVERED,
    PARTIALLY_REFUNDED
}