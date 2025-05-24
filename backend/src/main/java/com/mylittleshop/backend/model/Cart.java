package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts", indexes = {
    @Index(name = "idx_cart_user_id", columnList = "user_id"),
    @Index(name = "idx_cart_status", columnList = "status"),
    @Index(name = "idx_cart_expires_at", columnList = "expires_at")
})
@Getter @Setter
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CartStatus status = CartStatus.ACTIVE;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;
    
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String notes;
    
    // 편의 메서드: 장바구니 아이템 추가
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }
    
    // 편의 메서드: 장바구니 아이템 제거
    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }
    
    // 편의 메서드: 특정 상품이 장바구니에 있는지 확인
    public boolean containsProduct(Product product) {
        return cartItems.stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }
    
    // 편의 메서드: 특정 상품의 장바구니 아이템 찾기
    public CartItem findCartItemByProduct(Product product) {
        return cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
    }
    
    // 편의 메서드: 장바구니 비우기
    public void clear() {
        cartItems.clear();
    }
    
    // 편의 메서드: 활동 시간 업데이트
    public void updateActivity() {
        this.lastActivityDate = LocalDateTime.now();
    }
}