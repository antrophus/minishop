package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.RecurringOrder;
import com.mylittleshop.backend.model.Subscription;
import com.mylittleshop.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RecurringOrderRepository extends JpaRepository<RecurringOrder, Long> {
    
    // 사용자별 정기 주문 조회
    List<RecurringOrder> findByUser(User user);
    Page<RecurringOrder> findByUser(User user, Pageable pageable);
    
    // 사용자 ID별 정기 주문 조회
    List<RecurringOrder> findByUserId(Long userId);
    Page<RecurringOrder> findByUserId(Long userId, Pageable pageable);
    
    // 구독별 정기 주문 조회
    List<RecurringOrder> findBySubscription(Subscription subscription);
    Page<RecurringOrder> findBySubscription(Subscription subscription, Pageable pageable);
    
    // 구독 ID별 정기 주문 조회
    List<RecurringOrder> findBySubscriptionId(Long subscriptionId);
    Page<RecurringOrder> findBySubscriptionId(Long subscriptionId, Pageable pageable);
    
    // 상태별 정기 주문 조회
    List<RecurringOrder> findByStatus(String status);
    Page<RecurringOrder> findByStatus(String status, Pageable pageable);
    
    // 사용자 및 상태 조합 조회
    List<RecurringOrder> findByUserAndStatus(User user, String status);
    Page<RecurringOrder> findByUserAndStatus(User user, String status, Pageable pageable);
    
    // 주문 주기별 조회
    List<RecurringOrder> findByFrequency(String frequency);
    Page<RecurringOrder> findByFrequency(String frequency, Pageable pageable);
    
    // 다음 주문일 범위로 조회
    List<RecurringOrder> findByNextOrderDateBetween(LocalDate startDate, LocalDate endDate);
    Page<RecurringOrder> findByNextOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // 다음 주문일이 특정 날짜인 정기 주문 조회
    List<RecurringOrder> findByNextOrderDate(LocalDate orderDate);
    Page<RecurringOrder> findByNextOrderDate(LocalDate orderDate, Pageable pageable);
    
    // 다음 주문일이 오늘 또는 지난 정기 주문 조회 (처리가 필요한 주문)
    @Query("SELECT ro FROM RecurringOrder ro WHERE ro.nextOrderDate <= CURRENT_DATE AND ro.status = 'ACTIVE'")
    List<RecurringOrder> findOrdersDueForProcessing();
    
    // 특정 기간에 마지막 주문이 발생한 정기 주문 조회
    List<RecurringOrder> findByLastOrderDateBetween(LocalDate startDate, LocalDate endDate);
    Page<RecurringOrder> findByLastOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // 활성 상태이고 다음 주문일이 지정된 날짜 범위 내인 정기 주문 수 조회
    @Query("SELECT COUNT(ro) FROM RecurringOrder ro WHERE ro.status = 'ACTIVE' AND ro.nextOrderDate BETWEEN :startDate AND :endDate")
    Long countActiveOrdersByNextOrderDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    // 각 사용자별 활성 정기 주문 수 조회 (상위 N명)
    @Query("SELECT ro.user.id, COUNT(ro) as orderCount " +
           "FROM RecurringOrder ro " +
           "WHERE ro.status = 'ACTIVE' " +
           "GROUP BY ro.user.id " +
           "ORDER BY orderCount DESC")
    List<Object[]> countActiveOrdersByUser(Pageable pageable);
    
    // 주기별 정기 주문 분포 조회
    @Query("SELECT ro.frequency, COUNT(ro) " +
           "FROM RecurringOrder ro " +
           "WHERE ro.status = 'ACTIVE' " +
           "GROUP BY ro.frequency")
    List<Object[]> countActiveOrdersByFrequency();
}