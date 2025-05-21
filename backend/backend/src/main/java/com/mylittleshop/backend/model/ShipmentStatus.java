package com.mylittleshop.backend.model;

public enum ShipmentStatus {
    PENDING,
    PROCESSING,
    READY_FOR_PICKUP,
    PICKED_UP,
    IN_TRANSIT,
    DELAYED,
    DELIVERED,
    RETURNED,
    FAILED_DELIVERY,
    CANCELLED
}