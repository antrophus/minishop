package com.mylittleshop.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_products_name", columnList = "name"),
    @Index(name = "idx_products_status", columnList = "status"),
    @Index(name = "idx_products_sku", columnList = "sku")
})
@Getter @Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    // 소비자가 (General Market Price)
    @Column(name = "gm_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal gmPrice;
    
    // 회원가 (Gemma Member Price)
    @Column(name = "gbm_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal gbmPrice;
    
    // 총판가 (Shop Price)
    @Column(name = "shop_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal shopPrice;
    
    // 원가 (Cost Price) - 내부 계산용
    @Column(name = "cost_price", precision = 12, scale = 2)
    private BigDecimal costPrice;

    // 원래 소비자가 (Original GM Price) - 할인 전 가격
    @Column(name = "original_gm_price", precision = 12, scale = 2)
    private BigDecimal originalGmPrice;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductMedia> mediaItems = new ArrayList<>();

    // 편의 메서드 추가
    public void addMedia(ProductMedia media) {
        mediaItems.add(media);
        media.setProduct(this);
    }

    public void removeMedia(ProductMedia media) {
        mediaItems.remove(media);
        media.setProduct(null);
    }

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductContent content;

    @ManyToMany
    @JoinTable(name = "product_promotion",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private Set<Promotion> promotions = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttributeValue> attributeValues = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories = new ArrayList<>();
    
    @Column(nullable = false)
    private Boolean featured = false;
    
    @Column(nullable = false)
    private Boolean bestseller = false;
    
    @Column(name = "new_arrival", nullable = false)
    private Boolean newArrival = false;
    
    @Column(length = 50, unique = true)
    private String sku;
    
    @Column(length = 50)
    private String barcode;
    
    @Column(length = 100)
    private String brand;
    
    @Column(length = 100)
    private String manufacturer;
    
    // 최소 주문 수량
    @Column(name = "minimum_order_quantity", nullable = false)
    private Integer minimumOrderQuantity = 1;
    
    // 최대 주문 수량 (0 = 무제한)
    @Column(name = "maximum_order_quantity", nullable = false)
    private Integer maximumOrderQuantity = 0;
    
    // 구독 가능 상품 여부
    @Column(name = "subscription_available", nullable = false)
    private Boolean subscriptionAvailable = false;
    
    // 추천 구독 주기(일)
    @Column(name = "recommended_subscription_period")
    private Integer recommendedSubscriptionPeriod;
    
    // 배송비
    @Column(name = "shipping_fee", precision = 12, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;
    
    // 무료 배송 기준 금액
    @Column(name = "free_shipping_threshold", precision = 12, scale = 2)
    private BigDecimal freeShippingThreshold;
    
    // 원산지
    @Column(name = "country_of_origin", length = 100)
    private String countryOfOrigin;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal weight;
    
    @Column(name = "original_weight", precision = 10, scale = 2)
    private BigDecimal originalWeight;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 상품 가격 정보 반환 메서드 - 가격 유형별
    public BigDecimal getPriceByType(PriceType priceType) {
        return switch (priceType) {
            case GM -> gmPrice;
            case GBM -> gbmPrice;
            case SHOP -> shopPrice;
            case COST -> costPrice;
        };
    }
    
    // 편의 메서드: 재고 감소
    public void removeStock(int quantity) {
        // Inventory를 통해 재고 관리하도록 수정
        Inventory inventory = this.getMainInventory();
        if (inventory != null) {
            inventory.removeStock(quantity);
            // Product의 stockQuantity도 함께 업데이트
            this.stockQuantity = inventory.getQuantity();
            if (this.stockQuantity == 0) {
                this.status = ProductStatus.SOLD_OUT;
            }
        } else {
            // 기존 로직 유지
            int restStock = this.stockQuantity - quantity;
            if (restStock < 0) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }
            this.stockQuantity = restStock;
            if (this.stockQuantity == 0) {
                this.status = ProductStatus.SOLD_OUT;
            }
        }
    }
    
    // 편의 메서드: 재고 증가
    public void addStock(int quantity) {
        // Inventory를 통해 재고 관리하도록 수정
        Inventory inventory = this.getMainInventory();
        if (inventory != null) {
            inventory.addStock(quantity);
            // Product의 stockQuantity도 함께 업데이트
            this.stockQuantity = inventory.getQuantity();
            if (this.stockQuantity > 0 && this.status == ProductStatus.SOLD_OUT) {
                this.status = ProductStatus.ACTIVE;
            }
        } else {
            // 기존 로직 유지
            this.stockQuantity += quantity;
            if (this.stockQuantity > 0 && this.status == ProductStatus.SOLD_OUT) {
                this.status = ProductStatus.ACTIVE;
            }
        }
    }

    // 주 재고 항목 가져오기 메서드 추가
    private Inventory getMainInventory() {
        return this.inventories.stream().findFirst().orElse(null);
    }
    
    // 편의 메서드: 이미지 추가
    public void addImage(ProductImage image) {
        this.images.add(image);
        image.setProduct(this);
    }
    
    // 편의 메서드: 이미지 제거
    public void removeImage(ProductImage image) {
        this.images.remove(image);
        image.setProduct(null);
    }
    
    // 편의 메서드: 상품 속성 값 추가
    public void addAttributeValue(ProductAttributeValue attributeValue) {
        attributeValues.add(attributeValue);
        attributeValue.setProduct(this);
    }
    
    // 편의 메서드: 상품 속성 값 제거
    public void removeAttributeValue(ProductAttributeValue attributeValue) {
        attributeValues.remove(attributeValue);
        attributeValue.setProduct(null);
    }
    
    // 편의 메서드: 속성 코드별 속성 값 조회
    public Optional<ProductAttributeValue> getAttributeValueByCode(String attributeCode) {
        return attributeValues.stream()
            .filter(av -> av.getAttribute().getCode().equals(attributeCode))
            .findFirst();
    }
    
    // 편의 메서드: 인벤토리 추가
    public void addInventory(Inventory inventory) {
        inventories.add(inventory);
        inventory.setProduct(this);
    }
    
    // 편의 메서드: 인벤토리 제거
    public void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
        inventory.setProduct(null);
    }
    
    // 편의 메서드: 총 재고 수량 계산
    public int getTotalStock() {
        return inventories.stream()
            .mapToInt(Inventory::getQuantity)
            .sum();
    }
    
    // 편의 메서드: 총 가용 재고 수량 계산
    public int getTotalAvailableStock() {
        return inventories.stream()
            .mapToInt(Inventory::getAvailableStock)
            .sum();
    }
    
    // 편의 메서드: 재고 여부 확인
    public boolean isInStock() {
        return getTotalAvailableStock() > 0;
    }
    
    // 편의 메서드: 소비자가 할인 적용
    public void applyGmDiscount(BigDecimal discountRate) {
        if (this.originalGmPrice == null) {
            this.originalGmPrice = this.gmPrice;
        }
        
        BigDecimal discountAmount = this.originalGmPrice.multiply(discountRate.divide(BigDecimal.valueOf(100)));
        this.gmPrice = this.originalGmPrice.subtract(discountAmount);
    }
    
    // 가격 유형 열거형
    public enum PriceType {
        GM,     // 소비자가
        GBM,    // 회원가
        SHOP,   // 총판가
        COST    // 원가
    }
}