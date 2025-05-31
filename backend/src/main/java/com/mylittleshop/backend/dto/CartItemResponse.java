package com.mylittleshop.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 장바구니 아이템 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    
    private Long id;
    
    private Long productId;
    
    private String productName;
    
    private String productImage;
    
    private BigDecimal productPrice;
    
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    
    private String appliedPriceType;
    
    private LocalDateTime addedAt;
    
    // 추가 상품 정보
    private String productDescription;
    
    private String productSku;
    
    private String productBrand;
    
    private Integer stockQuantity;
    
    private Boolean inStock;
    
    @Override
    public String toString() {
        return "CartItemResponse{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", appliedPriceType='" + appliedPriceType + '\'' +
                '}';
    }
}
