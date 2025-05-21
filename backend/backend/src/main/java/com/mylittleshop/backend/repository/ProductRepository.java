package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.ProductCategory;
import com.mylittleshop.backend.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // 카테고리별 상품 조회
    List<Product> findByCategory(ProductCategory category);
    Page<Product> findByCategory(ProductCategory category, Pageable pageable);
    
    // 상태별 상품 조회
    List<Product> findByStatus(ProductStatus status);
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    
    // 활성 상태인 상품 조회
    List<Product> findByIsActive(Boolean isActive);
    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);
    
    // 상태 및 활성화 상태 조합 조회
    List<Product> findByStatusAndIsActive(ProductStatus status, Boolean isActive);
    Page<Product> findByStatusAndIsActive(ProductStatus status, Boolean isActive, Pageable pageable);
    
    // 가격 범위 조회 (소비자가 기준)
    List<Product> findByGmPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    Page<Product> findByGmPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    // 가격 범위 및 카테고리 조합 조회
    List<Product> findByGmPriceBetweenAndCategory(BigDecimal minPrice, BigDecimal maxPrice, ProductCategory category);
    Page<Product> findByGmPriceBetweenAndCategory(BigDecimal minPrice, BigDecimal maxPrice, ProductCategory category, Pageable pageable);
    
    // 이름으로 검색
    List<Product> findByNameContaining(String name);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    // SKU로 검색
    Optional<Product> findBySku(String sku);
    
    // 추천 상품 조회
    List<Product> findByFeaturedTrue();
    Page<Product> findByFeaturedTrue(Pageable pageable);
    
    // 베스트셀러 상품 조회
    List<Product> findByBestsellerTrue();
    Page<Product> findByBestsellerTrue(Pageable pageable);
    
    // 신상품 조회
    List<Product> findByNewArrivalTrue();
    Page<Product> findByNewArrivalTrue(Pageable pageable);
    
    // 브랜드별 상품 조회
    List<Product> findByBrand(String brand);
    Page<Product> findByBrand(String brand, Pageable pageable);
    
    // 재고 수량 범위 조회
    List<Product> findByStockQuantityBetween(Integer minQuantity, Integer maxQuantity);
    Page<Product> findByStockQuantityBetween(Integer minQuantity, Integer maxQuantity, Pageable pageable);
    
    // 재고 부족 상품 조회 (특정 수량 이하)
    List<Product> findByStockQuantityLessThan(Integer threshold);
    Page<Product> findByStockQuantityLessThan(Integer threshold, Pageable pageable);
    
    // 품절 상품 조회 (재고 0 또는 품절 상태)
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0 OR p.status = 'SOLD_OUT'")
    List<Product> findSoldOutProducts();
    Page<Product> findByStockQuantityEquals(Integer quantity, Pageable pageable);
    
    // 구독 가능 상품 조회
    List<Product> findBySubscriptionAvailableTrue();
    Page<Product> findBySubscriptionAvailableTrue(Pageable pageable);
    
    // 카테고리 및 구독 가능 여부 조합 조회
    List<Product> findByCategoryAndSubscriptionAvailableTrue(ProductCategory category);
    Page<Product> findByCategoryAndSubscriptionAvailableTrue(ProductCategory category, Pageable pageable);
    
    // 특정 가격 이하의 상품 조회
    List<Product> findByGmPriceLessThanEqual(BigDecimal maxPrice);
    Page<Product> findByGmPriceLessThanEqual(BigDecimal maxPrice, Pageable pageable);
    
    // 특정 가격 이상의 상품 조회
    List<Product> findByGmPriceGreaterThanEqual(BigDecimal minPrice);
    Page<Product> findByGmPriceGreaterThanEqual(BigDecimal minPrice, Pageable pageable);
    
    // 복합 조건 조회
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.status = :status AND p.isActive = true AND p.gmPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryStatusAndPriceRange(
            @Param("category") ProductCategory category,
            @Param("status") ProductStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.status = :status AND p.isActive = true AND p.gmPrice BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByCategoryStatusAndPriceRange(
            @Param("category") ProductCategory category,
            @Param("status") ProductStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
    
    // 할인된 상품 조회 (원래 가격과 현재 가격이 다른 경우)
    @Query("SELECT p FROM Product p WHERE p.originalGmPrice IS NOT NULL AND p.originalGmPrice > p.gmPrice")
    List<Product> findDiscountedProducts();
    
    @Query("SELECT p FROM Product p WHERE p.originalGmPrice IS NOT NULL AND p.originalGmPrice > p.gmPrice")
    Page<Product> findDiscountedProducts(Pageable pageable);
    
    // 무료 배송 상품 조회
    List<Product> findByShippingFeeEquals(BigDecimal zero);
    Page<Product> findByShippingFeeEquals(BigDecimal zero, Pageable pageable);
}