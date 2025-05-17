package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "promotions")
@Getter @Setter
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer discountRate;

    @ManyToMany(mappedBy = "promotions")
    private Set<Product> products = new HashSet<>();
} 