package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Order;
import com.mylittleshop.backend.model.OrderStatus;
import com.mylittleshop.backend.model.OrderStatusHistory;
import com.mylittleshop.backend.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    /**
     * 주문 상태 변경 이력 추가
     * 
     * @param order 주문
     * @param previousStatus 이전 상태
     * @param newStatus 새로운 상태
     * @param changedBy 변경자
     * @param changeReason 변경 사유
     * @return 생성된 주문 상태 이력
     */
    @Transactional
    public OrderStatusHistory addStatusHistory(Order order, OrderStatus previousStatus, OrderStatus newStatus, 
            String changedBy, String changeReason) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(changedBy);
        history.setChangeReason(changeReason);
        return orderStatusHistoryRepository.save(history);
    }

    /**
     * 주문 상태 변경 이력 추가 (간소화된 버전)
     * 
     * @param order 주문
     * @param newStatus 새로운 상태
     * @return 생성된 주문 상태 이력
     */
    @Transactional
    public OrderStatusHistory addStatusHistory(Order order, OrderStatus newStatus) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setPreviousStatus(order.getStatus());
        history.setNewStatus(newStatus);
        return orderStatusHistoryRepository.save(history);
    }

    /**
     * 주문별 상태 이력 조회
     * 
     * @param order 주문
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByOrder(Order order) {
        return orderStatusHistoryRepository.findByOrderOrderByChangedAtDesc(order);
    }

    /**
     * 주문 ID별 상태 이력 조회
     * 
     * @param orderId 주문 ID
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByOrderId(Long orderId) {
        return orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(orderId);
    }

    /**
     * 주문별 상태 이력 페이징 조회
     * 
     * @param order 주문
     * @param pageable 페이징 정보
     * @return 상태 이력 페이지
     */
    @Transactional(readOnly = true)
    public Page<OrderStatusHistory> getHistoryByOrder(Order order, Pageable pageable) {
        return orderStatusHistoryRepository.findByOrder(order, pageable);
    }

    /**
     * 주문 ID별 상태 이력 페이징 조회
     * 
     * @param orderId 주문 ID
     * @param pageable 페이징 정보
     * @return 상태 이력 페이지
     */
    @Transactional(readOnly = true)
    public Page<OrderStatusHistory> getHistoryByOrderId(Long orderId, Pageable pageable) {
        return orderStatusHistoryRepository.findByOrderId(orderId, pageable);
    }

    /**
     * 특정 상태로 변경된 이력 조회
     * 
     * @param status 상태
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByNewStatus(OrderStatus status) {
        return orderStatusHistoryRepository.findByNewStatus(status);
    }

    /**
     * 특정 이전 상태에서 변경된 이력 조회
     * 
     * @param status 이전 상태
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByPreviousStatus(OrderStatus status) {
        return orderStatusHistoryRepository.findByPreviousStatus(status);
    }

    /**
     * 특정 상태에서 특정 상태로 변경된 이력 조회
     * 
     * @param previousStatus 이전 상태
     * @param newStatus 새로운 상태
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByStatusChange(OrderStatus previousStatus, OrderStatus newStatus) {
        return orderStatusHistoryRepository.findByPreviousStatusAndNewStatus(previousStatus, newStatus);
    }

    /**
     * 특정 기간에 변경된 상태 이력 조회
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderStatusHistoryRepository.findByChangedAtBetween(startDate, endDate);
    }

    /**
     * 특정 사용자에 의해 변경된 상태 이력 조회
     * 
     * @param changedBy 변경자
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByChangedBy(String changedBy) {
        return orderStatusHistoryRepository.findByChangedBy(changedBy);
    }

    /**
     * 특정 사유로 변경된 상태 이력 조회
     * 
     * @param reasonPart 사유 부분 문자열
     * @return 상태 이력 목록
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getHistoryByChangeReason(String reasonPart) {
        return orderStatusHistoryRepository.findByChangeReasonContaining(reasonPart);
    }

    /**
     * 특정 주문의 가장 최근 상태 이력 조회
     * 
     * @param orderId 주문 ID
     * @return 최근 상태 이력
     */
    @Transactional(readOnly = true)
    public Optional<OrderStatusHistory> getLatestHistoryByOrderId(Long orderId) {
        return Optional.ofNullable(orderStatusHistoryRepository.findLatestByOrderId(orderId));
    }

    /**
     * 특정 기간 내 상태별 변경 횟수 통계
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 상태별 변경 횟수 통계
     */
    @Transactional(readOnly = true)
    public List<Object[]> getStatusChangeStatisticsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderStatusHistoryRepository.countStatusChangesByStatusBetweenDates(startDate, endDate);
    }
}