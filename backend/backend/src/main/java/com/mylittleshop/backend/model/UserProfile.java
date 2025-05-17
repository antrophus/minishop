package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter @Setter
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100)
    private String nickname;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 20)
    private String gender;

    @Column(length = 255)
    private String bio;
} 