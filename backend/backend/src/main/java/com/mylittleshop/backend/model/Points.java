package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "points")
@Getter @Setter
@NoArgsConstructor
public class Points {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "points", cascade = CascadeType.ALL)
    private List<PointTransaction> transactions = new ArrayList<>();
    
    // 편의 메서드: 포인트 적립
    public PointTransaction addPoints(int amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("적립 포인트는 0보다 커야 합니다.");
        }
        
        totalPoints += amount;
        
        PointTransaction transaction = new PointTransaction();
        transaction.setPoints(this);
        transaction.setUser(user);
        transaction.setChangeType("EARN");
        transaction.setAmount(amount);
        transaction.setDescription(description);
        
        transactions.add(transaction);
        
        return transaction;
    }
    
    // 편의 메서드: 포인트 사용
    public PointTransaction usePoints(int amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용 포인트는 0보다 커야 합니다.");
        }
        
        if (totalPoints < amount) {
            throw new IllegalArgumentException("사용 가능한 포인트가 부족합니다.");
        }
        
        totalPoints -= amount;
        
        PointTransaction transaction = new PointTransaction();
        transaction.setPoints(this);
        transaction.setUser(user);
        transaction.setChangeType("USE");
        transaction.setAmount(-amount); // 음수로 저장
        transaction.setDescription(description);
        
        transactions.add(transaction);
        
        return transaction;
    }
    
    // 편의 메서드: 포인트 만료
    public PointTransaction expirePoints(int amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("만료 포인트는 0보다 커야 합니다.");
        }
        
        if (totalPoints < amount) {
            amount = totalPoints; // 가용 포인트 내에서만 만료
        }
        
        totalPoints -= amount;
        
        PointTransaction transaction = new PointTransaction();
        transaction.setPoints(this);
        transaction.setUser(user);
        transaction.setChangeType("EXPIRE");
        transaction.setAmount(-amount); // 음수로 저장
        transaction.setDescription(description);
        
        transactions.add(transaction);
        
        return transaction;
    }
}