package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.RecurringOrder;
import com.mylittleshop.backend.model.RecurringOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RecurringOrderItemRepository extends JpaRepository<RecurringOrderItem, Long> {
    
    // 정기 주문별 아이템 조회
    List<RecurringOrderItem> findByRecurringOrder(RecurringOrder recurringOrder);
    Page<RecurringOrderItem> findByRecurringOrder(RecurringOrder recurringOrder, Pageable pageable);
    
    // 정기 주문 ID별 아이템 조회
    List<RecurringOrderItem> findByRecurringOrderId(Long recurringOrderId);
    Page<RecurringOrderItem> findByRecurringOrderId(Long recurringOrderId, Pageable pageable);
    
    // 상품별 정기 주문 아이템 조회
    List<RecurringOrderItem> findByProduct(Product product);
    Page<RecurringOrderItem> findByProduct(Product product, Pageable pageable);
    
    // 상품 ID별 정기 주문 아이템 조회
    List<RecurringOrderItem> findByProductId(Long productId);
    Page<RecurringOrderItem> findByProductId(Long productId, Pageable pageable);
    
    // 정기 주문 및 상품 조합 조회
    List<RecurringOrderItem> findByRecurringOrderAndProduct(RecurringOrder recurringOrder, Product product);
    
    // 정기 주문 ID 및 상품 ID 조합 조회
    List<RecurringOrderItem> findByRecurringOrderIdAndProductId(Long recurringOrderId, Long productId);
    
    // 수량별 정기 주문 아이템 조회
    List<RecurringOrderItem> findByQuantity(Integer quantity);
    Page<RecurringOrderItem> findByQuantity(Integer quantity, Pageable pageable);
    
    // 단가 범위로 정기 주문 아이템 조회
    List<RecurringOrderItem> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    Page<RecurringOrderItem> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    // 가격 유형별 정기 주문 아이템 조회
    List<RecurringOrderItem> findByAppliedPriceType(String priceType);
    Page<RecurringOrderItem> findByAppliedPriceType(String priceType, Pageable pageable);
    
    // 선물 여부로 정기 주문 아이템 조회
    List<RecurringOrderItem> findByIsGiftTrue();
    Page<RecurringOrderItem> findByIsGiftTrue(Pageable pageable);
    
    // 특정 정기 주문의 아이템 수 조회
    long countByRecurringOrder(RecurringOrder recurringOrder);
    long countByRecurringOrderId(Long recurringOrderId);
    
    // 특정 정기 주문의 총 상품 금액 계산
    @Query("SELECT SUM(roi.quantity * roi.unitPrice) FROM RecurringOrderItem roi WHERE roi.recurringOrder.id = :recurringOrderId")
    BigDecimal calculateTotalAmountByRecurringOrderId(@Param("recurringOrderId") Long recurringOrderId);
    
    // 특정 상품이 포함된 활성 정기 주문 아이템 수 조회
    @Query("SELECT COUNT(roi) FROM RecurringOrderItem roi JOIN roi.recurringOrder ro WHERE roi.product.id = :productId AND ro.status = 'ACTIVE'")
    long countActiveRecurringOrdersWithProduct(@Param("productId") Long productId);
    
    // 많이 주문되는 상품 순위 조회
    @Query("SELECT roi.product.id, SUM(roi.quantity) as totalQuantity " +
           "FROM RecurringOrderItem roi JOIN roi.recurringOrder ro " +
           "WHERE ro.status = 'ACTIVE' " +
           "GROUP BY roi.product.id " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findMostOrderedProducts(Pageable pageable);
}