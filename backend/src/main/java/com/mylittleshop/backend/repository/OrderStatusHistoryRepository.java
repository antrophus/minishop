package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Order;
import com.mylittleshop.backend.model.OrderStatus;
import com.mylittleshop.backend.model.OrderStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    
    // 주문별 상태 이력 조회
    List<OrderStatusHistory> findByOrder(Order order);
    Page<OrderStatusHistory> findByOrder(Order order, Pageable pageable);
    
    // 주문 ID별 상태 이력 조회
    List<OrderStatusHistory> findByOrderId(Long orderId);
    Page<OrderStatusHistory> findByOrderId(Long orderId, Pageable pageable);
    
    // 주문별 상태 이력 역시간순 조회
    List<OrderStatusHistory> findByOrderOrderByChangedAtDesc(Order order);
    List<OrderStatusHistory> findByOrderIdOrderByChangedAtDesc(Long orderId);
    
    // 특정 상태로 변경된 이력 조회
    List<OrderStatusHistory> findByNewStatus(OrderStatus status);
    Page<OrderStatusHistory> findByNewStatus(OrderStatus status, Pageable pageable);
    
    // 특정 이전 상태에서 변경된 이력 조회
    List<OrderStatusHistory> findByPreviousStatus(OrderStatus status);
    Page<OrderStatusHistory> findByPreviousStatus(OrderStatus status, Pageable pageable);
    
    // 특정 상태에서 특정 상태로 변경된 이력 조회
    List<OrderStatusHistory> findByPreviousStatusAndNewStatus(OrderStatus previousStatus, OrderStatus newStatus);
    Page<OrderStatusHistory> findByPreviousStatusAndNewStatus(OrderStatus previousStatus, OrderStatus newStatus, Pageable pageable);
    
    // 특정 기간에 변경된 상태 이력 조회
    List<OrderStatusHistory> findByChangedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<OrderStatusHistory> findByChangedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 특정 사용자에 의해 변경된 상태 이력 조회
    List<OrderStatusHistory> findByChangedBy(String changedBy);
    Page<OrderStatusHistory> findByChangedBy(String changedBy, Pageable pageable);
    
    // 특정 사유로 변경된 상태 이력 조회
    List<OrderStatusHistory> findByChangeReasonContaining(String reasonPart);
    Page<OrderStatusHistory> findByChangeReasonContaining(String reasonPart, Pageable pageable);
    
    // 특정 주문의 가장 최근 상태 이력 조회
    @Query("SELECT osh FROM OrderStatusHistory osh WHERE osh.order.id = :orderId ORDER BY osh.changedAt DESC LIMIT 1")
    OrderStatusHistory findLatestByOrderId(@Param("orderId") Long orderId);
    
    // 특정 기간 내 상태별 변경 횟수 통계
    @Query("SELECT osh.newStatus, COUNT(osh) " +
           "FROM OrderStatusHistory osh " +
           "WHERE osh.changedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY osh.newStatus")
    List<Object[]> countStatusChangesByStatusBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}