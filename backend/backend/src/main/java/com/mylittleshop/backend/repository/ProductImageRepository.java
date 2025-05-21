package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    
    // 상품별 이미지 조회
    List<ProductImage> findByProduct(Product product);
    Page<ProductImage> findByProduct(Product product, Pageable pageable);
    
    // 상품 ID별 이미지 조회
    List<ProductImage> findByProductId(Long productId);
    Page<ProductImage> findByProductId(Long productId, Pageable pageable);
    
    // 상품별 정렬 순서에 따른 이미지 조회
    List<ProductImage> findByProductOrderBySortOrderAsc(Product product);
    
    // 상품 ID별 정렬 순서에 따른 이미지 조회
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);
    
    // 메인 이미지 조회
    Optional<ProductImage> findByProductAndIsMainTrue(Product product);
    Optional<ProductImage> findByProductIdAndIsMainTrue(Long productId);
    
    // 이전 메인 이미지 상태 변경
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.isMain = false WHERE pi.product.id = :productId AND pi.isMain = true")
    void unsetMainImageForProduct(@Param("productId") Long productId);
    
    // URL로 이미지 검색
    List<ProductImage> findByUrlContaining(String urlPart);
    
    // 특정 상품의 이미지 수 조회
    long countByProduct(Product product);
    long countByProductId(Long productId);
    
    // 특정 상품의 이미지 모두 삭제
    void deleteByProduct(Product product);
    void deleteByProductId(Long productId);
}