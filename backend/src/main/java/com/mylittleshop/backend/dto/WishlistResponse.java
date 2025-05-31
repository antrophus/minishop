package com.mylittleshop.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 위시리스트 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponse {
    
    private Long id;
    
    private Long productId;
    
    private String productName;
    
    private String productImage;
    
    private BigDecimal productPrice;
    
    private String productDescription;
    
    private LocalDateTime createdAt;
    
    // 추가 상품 정보
    private String productSku;
    
    private String productBrand;
    
    private Integer stockQuantity;
    
    private Boolean inStock;
    
    private Boolean isActive;
    
    // 위시리스트에서 장바구니 추가 가능 여부
    private Boolean canAddToCart;
    
    @Override
    public String toString() {
        return "WishlistResponse{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", createdAt=" + createdAt +
                '}';
    }
}
