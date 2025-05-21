package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_username", columnList = "username"),
    @Index(name = "idx_users_email", columnList = "email")
})
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(name = "is_gemma_member", nullable = false)
    private Boolean isGemmaMember = false;

    @Column(length = 100)
    private String name;

    @Column(length = 10)
    private String gender;

    // 활성화 상태
    @Column(nullable = false)
    private Boolean active = true;

    // 이메일 인증 완료 여부
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;
    
    // 계정 잠금 여부
    @Column(nullable = false)
    private Boolean locked = false;

    // 계정 만료일
    @Column(name = "account_expire_date")
    private LocalDate accountExpireDate;

    // 마지막 로그인 시간
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // 실패한 로그인 시도 횟수
    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DeliveryAddress> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Wishlist> wishlists = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 편의 메서드: 역할 추가
    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    // 편의 메서드: 역할 제거
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    // 편의 메서드: 배송 주소 추가
    public void addDeliveryAddress(DeliveryAddress address) {
        addresses.add(address);
        address.setUser(this);
    }

    // 편의 메서드: 배송 주소 제거
    public void removeDeliveryAddress(DeliveryAddress address) {
        addresses.remove(address);
        address.setUser(null);
    }
}