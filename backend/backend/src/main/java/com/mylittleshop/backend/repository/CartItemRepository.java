package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Cart;
import com.mylittleshop.backend.model.CartItem;
import com.mylittleshop.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // 장바구니별 아이템 조회
    List<CartItem> findByCart(Cart cart);
    Page<CartItem> findByCart(Cart cart, Pageable pageable);
    
    // 장바구니 ID별 아이템 조회
    List<CartItem> findByCartId(Long cartId);
    Page<CartItem> findByCartId(Long cartId, Pageable pageable);
    
    // 상품별 장바구니 아이템 조회
    List<CartItem> findByProduct(Product product);
    Page<CartItem> findByProduct(Product product, Pageable pageable);
    
    // 상품 ID별 장바구니 아이템 조회
    List<CartItem> findByProductId(Long productId);
    Page<CartItem> findByProductId(Long productId, Pageable pageable);
    
    // 장바구니 및 상품 조합 조회
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    
    // 수량별 장바구니 아이템 조회
    List<CartItem> findByQuantity(int quantity);
    List<CartItem> findByQuantityGreaterThan(int quantity);
    List<CartItem> findByQuantityLessThan(int quantity);
    
    // 추가 날짜별 장바구니 아이템 조회
    List<CartItem> findByAddedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<CartItem> findByAddedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 특정 날짜 이후 장바구니 아이템 조회
    List<CartItem> findByAddedAtAfter(LocalDateTime date);
    Page<CartItem> findByAddedAtAfter(LocalDateTime date, Pageable pageable);
    
    // 가격별 장바구니 아이템 조회
    Page<CartItem> findByUnitPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);    
    
    // 적용된 가격 유형별 장바구니 아이템 조회
    List<CartItem> findByAppliedPriceType(String priceType);
    Page<CartItem> findByAppliedPriceType(String priceType, Pageable pageable);
    
    // 장바구니별 아이템 카운트
    long countByCartId(Long cartId);
    
    // 상품별 장바구니 아이템 카운트
    long countByProductId(Long productId);
    
    // 특정 가격 유형별 장바구니 아이템 카운트
    long countByAppliedPriceType(String priceType);
    
    // 장바구니별 총 아이템 금액 계산
    @Query("SELECT SUM(ci.quantity * ci.unitPrice) FROM CartItem ci WHERE ci.cart.id = :cartId")
    BigDecimal calculateTotalAmountByCartId(@Param("cartId") Long cartId);
    
    // 특정 카테고리의 상품이 포함된 장바구니 아이템 조회
    @Query("SELECT ci FROM CartItem ci WHERE ci.product.category.id = :categoryId")
    List<CartItem> findByProductCategoryId(@Param("categoryId") Long categoryId);
    
    // 장바구니 아이템 가장 많이 추가된 상품 조회
    @Query("SELECT ci.product.id, COUNT(ci) as itemCount " +
           "FROM CartItem ci " +
           "GROUP BY ci.product.id " +
           "ORDER BY itemCount DESC")
    List<Object[]> findMostAddedProducts(Pageable pageable);
    
    // 장바구니 아이템 가장 많이 추가된 상품 조회 (제한 수)
    @Query(value = "SELECT ci.product_id, COUNT(ci.id) as item_count " +
                   "FROM cart_item ci " +
                   "GROUP BY ci.product_id " +
                   "ORDER BY item_count DESC " +
                   "LIMIT :limit", 
            nativeQuery = true)
    List<Object[]> findMostAddedProducts(@Param("limit") int limit);
    
    // 사용자 ID별 장바구니 아이템 조회
    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.id = :userId AND c.status = 'ACTIVE'")
    List<CartItem> findByUserId(@Param("userId") Long userId);
    
    // 사용자 ID별 장바구니 아이템 페이징 조회
    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.id = :userId AND c.status = 'ACTIVE'")
    Page<CartItem> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    // 장바구니 삭제 시 아이템 일괄 삭제
    void deleteByCartId(Long cartId);
}