package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "product_attributes")
@Getter @Setter
@NoArgsConstructor
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<ProductAttributeValue> values = new ArrayList<>();
} 