package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.*;
import com.mylittleshop.backend.repository.RecurringOrderItemRepository;
import com.mylittleshop.backend.repository.RecurringOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecurringOrderService {

    private final RecurringOrderRepository recurringOrderRepository;
    private final RecurringOrderItemRepository recurringOrderItemRepository;
    private final OrderService orderService;

    /**
     * 정기 주문 조회
     * 
     * @param id 정기 주문 ID
     * @return 정기 주문 Optional
     */
    @Transactional(readOnly = true)
    public Optional<RecurringOrder> getRecurringOrderById(Long id) {
        return recurringOrderRepository.findById(id);
    }

    /**
     * 사용자별 정기 주문 조회
     * 
     * @param user 사용자
     * @return 정기 주문 목록
     */
    @Transactional(readOnly = true)
    public List<RecurringOrder> getRecurringOrdersByUser(User user) {
        return recurringOrderRepository.findByUser(user);
    }

    /**
     * 사용자별 정기 주문 페이징 조회
     * 
     * @param user 사용자
     * @param pageable 페이징 정보
     * @return 정기 주문 페이지
     */
    @Transactional(readOnly = true)
    public Page<RecurringOrder> getRecurringOrdersByUser(User user, Pageable pageable) {
        return recurringOrderRepository.findByUser(user, pageable);
    }

    /**
     * 사용자 ID별 정기 주문 조회
     * 
     * @param userId 사용자 ID
     * @return 정기 주문 목록
     */
    @Transactional(readOnly = true)
    public List<RecurringOrder> getRecurringOrdersByUserId(Long userId) {
        return recurringOrderRepository.findByUserId(userId);
    }

    /**
     * 구독별 정기 주문 조회
     * 
     * @param subscription 구독
     * @return 정기 주문 목록
     */
    @Transactional(readOnly = true)
    public List<RecurringOrder> getRecurringOrdersBySubscription(Subscription subscription) {
        return recurringOrderRepository.findBySubscription(subscription);
    }

    /**
     * 상태별 정기 주문 조회
     * 
     * @param status 상태
     * @return 정기 주문 목록
     */
    @Transactional(readOnly = true)
    public List<RecurringOrder> getRecurringOrdersByStatus(String status) {
        return recurringOrderRepository.findByStatus(status);
    }

    /**
     * 다음 주문일별 정기 주문 조회
     * 
     * @param nextOrderDate 다음 주문일
     * @return 정기 주문 목록
     */
    @Transactional(readOnly = true)
    public List<RecurringOrder> getRecurringOrdersByNextOrderDate(LocalDate nextOrderDate) {
        return recurringOrderRepository.findByNextOrderDate(nextOrderDate);
    }

    /**
     * 다음 주문일 범위로 정기 주문 조회
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 정기 주문 목록
     */
    @Transactional(readOnly = true)
    public List<RecurringOrder> getRecurringOrdersByNextOrderDateRange(LocalDate startDate, LocalDate endDate) {
        return recurringOrderRepository.findByNextOrderDateBetween(startDate, endDate);
    }

    /**
     * 처리가 필요한 정기 주문 조회 (다음 주문일이 오늘 또는 이전)
     * 
     * @return 정기 주문 목록
     */
    @Transactional(readOnly = true)
    public List<RecurringOrder> getRecurringOrdersDueForProcessing() {
        return recurringOrderRepository.findOrdersDueForProcessing();
    }

    /**
     * 정기 주문 생성
     * 
     * @param user 사용자
     * @param subscription 구독 (선택사항)
     * @param frequency 주기
     * @param nextOrderDate 다음 주문일
     * @param shippingAddressId 배송지 ID
     * @param paymentMethodId 결제 수단 ID
     * @return 생성된 정기 주문
     */
    @Transactional
    public RecurringOrder createRecurringOrder(User user, Subscription subscription, String frequency, 
            LocalDate nextOrderDate, Long shippingAddressId, Long paymentMethodId) {
        RecurringOrder recurringOrder = new RecurringOrder();
        recurringOrder.setUser(user);
        recurringOrder.setSubscription(subscription);
        recurringOrder.setFrequency(frequency);
        recurringOrder.setNextOrderDate(nextOrderDate);
        recurringOrder.setStatus("ACTIVE");
        recurringOrder.setShippingAddressId(shippingAddressId);
        recurringOrder.setPaymentMethodId(paymentMethodId);
        return recurringOrderRepository.save(recurringOrder);
    }

    /**
     * 정기 주문 아이템 추가
     * 
     * @param recurringOrder 정기 주문
     * @param product 상품
     * @param quantity 수량
     * @param price 가격
     * @return 생성된 정기 주문 아이템
     */
    @Transactional
    public RecurringOrderItem addRecurringOrderItem(RecurringOrder recurringOrder, Product product, 
            int quantity, double price) {
        RecurringOrderItem item = new RecurringOrderItem();
        item.setRecurringOrder(recurringOrder);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(BigDecimal.valueOf(price));
        return recurringOrderItemRepository.save(item);
    }

    /**
     * 정기 주문 아이템 수정
     * 
     * @param itemId 아이템 ID
     * @param quantity 수량
     * @return 수정된 정기 주문 아이템
     */
    @Transactional
    public Optional<RecurringOrderItem> updateRecurringOrderItemQuantity(Long itemId, int quantity) {
        return recurringOrderItemRepository.findById(itemId).map(item -> {
            item.setQuantity(quantity);
            return recurringOrderItemRepository.save(item);
        });
    }

    /**
     * 정기 주문 상태 업데이트
     * 
     * @param id 정기 주문 ID
     * @param status 상태
     * @return 업데이트된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> updateRecurringOrderStatus(Long id, String status) {
        return recurringOrderRepository.findById(id).map(order -> {
            order.setStatus(status);
            return recurringOrderRepository.save(order);
        });
    }

    /**
     * 정기 주문 취소
     * 
     * @param id 정기 주문 ID
     * @return 취소된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> cancelRecurringOrder(Long id) {
        return updateRecurringOrderStatus(id, "CANCELLED");
    }

    /**
     * 정기 주문 중지
     * 
     * @param id 정기 주문 ID
     * @return 중지된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> pauseRecurringOrder(Long id) {
        return updateRecurringOrderStatus(id, "PAUSED");
    }

    /**
     * 정기 주문 재개
     * 
     * @param id 정기 주문 ID
     * @return 재개된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> resumeRecurringOrder(Long id) {
        return updateRecurringOrderStatus(id, "ACTIVE");
    }

    /**
     * 정기 주문의 다음 주문일 업데이트
     * 
     * @param id 정기 주문 ID
     * @param nextOrderDate 다음 주문일
     * @return 업데이트된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> updateNextOrderDate(Long id, LocalDate nextOrderDate) {
        return recurringOrderRepository.findById(id).map(order -> {
            order.setNextOrderDate(nextOrderDate);
            return recurringOrderRepository.save(order);
        });
    }

    /**
     * 정기 주문의 주기 업데이트
     * 
     * @param id 정기 주문 ID
     * @param frequency 주기
     * @return 업데이트된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> updateFrequency(Long id, String frequency) {
        return recurringOrderRepository.findById(id).map(order -> {
            order.setFrequency(frequency);
            return recurringOrderRepository.save(order);
        });
    }

    /**
     * 정기 주문에서 실제 주문 생성
     * 
     * @param recurringOrderId 정기 주문 ID
     * @return 생성된 주문
     */
    @Transactional
    public Optional<Order> processRecurringOrder(Long recurringOrderId) {
        return recurringOrderRepository.findById(recurringOrderId).map(recurringOrder -> {
            if (!"ACTIVE".equals(recurringOrder.getStatus())) {
                throw new IllegalStateException("정기 주문이 활성 상태가 아닙니다.");
            }

            // 주문 생성
            Order order = new Order();
            order.setUser(recurringOrder.getUser());
            // 배송지, 결제 정보 등 설정
            
            // 주문 아이템 추가
            for (RecurringOrderItem recurringItem : recurringOrder.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(recurringItem.getProduct());
                orderItem.setQuantity(recurringItem.getQuantity());
                orderItem.setUnitPrice(recurringItem.getUnitPrice());
                order.addOrderItem(orderItem);
            }
            
            // 주문 저장
            Order savedOrder = orderService.createOrder(order);
            
            // 정기 주문 다음 주문일 업데이트
            recurringOrder.setLastOrderDate(LocalDate.now());
            recurringOrder.calculateNextOrderDate();
            recurringOrderRepository.save(recurringOrder);
            
            return savedOrder;
        });
    }

    /**
     * 오늘 처리해야 할 모든 정기 주문 일괄 처리
     * 
     * @return 생성된 주문 목록
     */
    @Transactional
    public List<Order> processDueRecurringOrders() {
        List<RecurringOrder> dueOrders = getRecurringOrdersDueForProcessing();
        List<Order> processedOrders = new ArrayList<>();
        
        for (RecurringOrder recurringOrder : dueOrders) {
            try {
                processRecurringOrder(recurringOrder.getId()).ifPresent(processedOrders::add);
            } catch (Exception e) {
                // 로깅 및 예외 처리
            }
        }
        
        return processedOrders;
    }

    /**
     * 정기 주문 배송지 업데이트
     * 
     * @param id 정기 주문 ID
     * @param shippingAddressId 배송지 ID
     * @return 업데이트된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> updateShippingAddress(Long id, Long shippingAddressId) {
        return recurringOrderRepository.findById(id).map(order -> {
            order.setShippingAddressId(shippingAddressId);
            return recurringOrderRepository.save(order);
        });
    }

    /**
     * 정기 주문 결제 수단 업데이트
     * 
     * @param id 정기 주문 ID
     * @param paymentMethodId 결제 수단 ID
     * @return 업데이트된 정기 주문
     */
    @Transactional
    public Optional<RecurringOrder> updatePaymentMethod(Long id, Long paymentMethodId) {
        return recurringOrderRepository.findById(id).map(order -> {
            order.setPaymentMethodId(paymentMethodId);
            return recurringOrderRepository.save(order);
        });
    }
}