package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_transactions")
@Getter @Setter
@NoArgsConstructor
public class PointTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_id")
    private Points points;
    
    @Column(name = "change_type", length = 30, nullable = false)
    private String changeType; // EARN, USE, EXPIRE
    
    @Column(nullable = false)
    private Integer amount;
    
    @Column(length = 255)
    private String description;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}