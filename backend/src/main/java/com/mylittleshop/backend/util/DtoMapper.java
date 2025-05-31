package com.mylittleshop.backend.util;

import com.mylittleshop.backend.dto.CartItemResponse;
import com.mylittleshop.backend.dto.WishlistResponse;
import com.mylittleshop.backend.model.CartItem;
import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.Wishlist;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO 변환 유틸리티 클래스
 */
@Component
public class DtoMapper {
    
    /**
     * CartItem을 CartItemResponse로 변환
     */
    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        
        Product product = cartItem.getProduct();
        BigDecimal totalPrice = cartItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImageUrl())
                .productPrice(product.getPrice())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(totalPrice)
                .appliedPriceType(cartItem.getAppliedPriceType())
                .addedAt(cartItem.getAddedAt())
                .productDescription(product.getDescription())
                .productSku(product.getSku())
                .productBrand(product.getBrand())
                .stockQuantity(product.getStockQuantity())
                .inStock(product.isInStock())
                .build();
    }
    
    /**
     * CartItem 목록을 CartItemResponse 목록으로 변환
     */
    public List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems) {
        if (cartItems == null) {
            return List.of();
        }
        
        return cartItems.stream()
                .map(this::toCartItemResponse)
                .toList();
    }
    
    /**
     * Wishlist를 WishlistResponse로 변환
     */
    public WishlistResponse toWishlistResponse(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }
        
        Product product = wishlist.getProduct();
        
        return WishlistResponse.builder()
                .id(wishlist.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImageUrl())
                .productPrice(product.getPrice())
                .productDescription(product.getDescription())
                .createdAt(wishlist.getCreatedAt())
                .productSku(product.getSku())
                .productBrand(product.getBrand())
                .stockQuantity(product.getStockQuantity())
                .inStock(product.isInStock())
                .isActive(product.getIsActive())
                .canAddToCart(product.isInStock() && product.getIsActive())
                .build();
    }    
    /**
     * Wishlist 목록을 WishlistResponse 목록으로 변환
     */
    public List<WishlistResponse> toWishlistResponseList(List<Wishlist> wishlists) {
        if (wishlists == null) {
            return List.of();
        }
        
        return wishlists.stream()
                .map(this::toWishlistResponse)
                .toList();
    }
}
