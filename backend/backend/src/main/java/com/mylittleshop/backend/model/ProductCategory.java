package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_categories")
@Getter @Setter
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductCategory parent;
    
    @OneToMany(mappedBy = "parent")
    private List<ProductCategory> children = new ArrayList<>();
    
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
    
    public ProductCategory(String name) {
        this.name = name;
    }
    
    // 편의 메서드: 하위 카테고리 추가
    public void addChild(ProductCategory child) {
        children.add(child);
        child.setParent(this);
    }
    
    // 편의 메서드: 하위 카테고리 제거
    public void removeChild(ProductCategory child) {
        children.remove(child);
        child.setParent(null);
    }
    
    // 편의 메서드: 상품 추가
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }
    
    // 편의 메서드: 상품 제거
    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }
    
    // 편의 메서드: 전체 경로 문자열 반환
    public String getFullPath() {
        if (parent == null) {
            return name;
        } else {
            return parent.getFullPath() + " > " + name;
        }
    }
    
    // 편의 메서드: 최상위 카테고리인지 확인
    public boolean isRoot() {
        return parent == null;
    }
    
    // 편의 메서드: 최하위 카테고리인지 확인
    public boolean isLeaf() {
        return children.isEmpty();
    }
}