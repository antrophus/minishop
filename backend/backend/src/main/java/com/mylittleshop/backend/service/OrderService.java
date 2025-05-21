package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.*;
import com.mylittleshop.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final ShipmentRepository shipmentRepository;
    
    // 기본 CRUD 작업
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
    
    // 고급 조회 기능
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }
    
    public Page<Order> findByUser(User user, Pageable pageable) {
        return orderRepository.findByUser(user, pageable);
    }
    
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }
    
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }
    
    public List<Order> findByUserAndStatus(User user, OrderStatus status) {
        return orderRepository.findByUserAndStatus(user, status);
    }
    
    public Page<Order> findByUserAndStatus(User user, OrderStatus status, Pageable pageable) {
        return orderRepository.findByUserAndStatus(user, status, pageable);
    }
    
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    public Page<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return orderRepository.findByOrderDateBetween(startDate, endDate, pageable);
    }
    
    public List<Order> findByPaymentMethod(String paymentMethod) {
        return orderRepository.findByPaymentMethod(paymentMethod);
    }
    
    public List<Order> findByShippingMethod(String shippingMethod) {
        return orderRepository.findByShippingMethod(shippingMethod);
    }
    
    public List<Order> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return orderRepository.findByTotalAmountBetween(minAmount, maxAmount);
    }
    
    public List<Order> findGiftOrders() {
        return orderRepository.findByIsGiftTrue();
    }
    
    public List<Order> findRecurringOrders() {
        return orderRepository.findByIsRecurringTrue();
    }
    
    public List<Object[]> getDailySalesSummary(LocalDateTime startDate) {
        return orderRepository.getDailySalesSummary(startDate);
    }
    
    public List<Object[]> getOrderStatusCounts() {
        return orderRepository.countOrdersByStatus();
    }
    
    // 비즈니스 로직
    @Transactional
    public Order createOrder(Long userId, List<OrderItem> orderItems, String paymentMethod, String shippingMethod,
                             Delivery delivery, BigDecimal shippingFee, BigDecimal taxAmount,
                             BigDecimal discountAmount, String couponCode, Boolean isGift, String giftMessage) {
        
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // 배송 정보 저장
        Delivery savedDelivery = deliveryRepository.save(delivery);
        
        // 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setDelivery(savedDelivery);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(paymentMethod);
        order.setShippingMethod(shippingMethod);
        order.setShippingFee(shippingFee);
        order.setTaxAmount(taxAmount);
        order.setDiscountAmount(discountAmount);
        order.setCouponCode(couponCode);
        order.setIsGift(isGift);
        order.setGiftMessage(giftMessage);
        order.setOrderDate(LocalDateTime.now());
        
        // 주문 저장
        Order savedOrder = orderRepository.save(order);
        
        // 주문 항목 처리
        for (OrderItem item : orderItems) {
            // 상품 조회
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProduct().getId()));
            
            // 재고 확인
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            // 재고 감소
            product.removeStock(item.getQuantity());
            productRepository.save(product);
            
            // 주문 항목 저장
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
            
            // 주문 금액 갱신
            savedOrder.addOrderItem(item);
        }
        
        // 최종 금액 계산
        savedOrder.setFinalAmount(
                savedOrder.getTotalAmount()
                        .subtract(savedOrder.getDiscountAmount())
                        .add(savedOrder.getShippingFee())
                        .add(savedOrder.getTaxAmount())
        );
        
        // 주문 상태 이력 추가
        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(savedOrder);
        statusHistory.setNewStatus(OrderStatus.PENDING);
        statusHistory.setChangedAt(LocalDateTime.now());
        orderStatusHistoryRepository.save(statusHistory);
        
        return orderRepository.save(savedOrder);
    }
    
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus, String reason, String changedBy) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.changeStatus(newStatus, reason, changedBy);
        return orderRepository.save(order);
    }
    
    @Transactional
    public Payment addPayment(Long orderId, BigDecimal amount, String paymentMethod, String transactionId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(transactionId);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentCompletionTimestamp(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // 결제 완료 시 주문 상태 변경
        if (order.isFullyPaid()) {
            order.changeStatus(OrderStatus.PROCESSING, "Payment completed", "SYSTEM");
        }
        
        return savedPayment;
    }
    
    @Transactional
    public Shipment createShipment(Long orderId, String trackingNumber, String carrier) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setCarrier(carrier);
        shipment.setStatus(ShipmentStatus.PENDING);
        
        // 배송지 정보 복사
        Delivery delivery = order.getDelivery();
        if (delivery != null) {
            shipment.setRecipientName(delivery.getRecipientName());
            shipment.setRecipientPhone(delivery.getContactNumber());
            shipment.setAddressLine1(delivery.getAddress());
        }
        
        Shipment savedShipment = shipmentRepository.save(shipment);
        
        return savedShipment;
    }
    
    @Transactional
    public Shipment updateShipmentStatus(Long shipmentId, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));
        
        shipment.setStatus(status);
        
        // 배송중 상태로 변경 시
        if (status == ShipmentStatus.IN_TRANSIT) {
            shipment.setShippedAt(LocalDateTime.now());
            
            // 주문 상태도 배송중으로 변경
            Order order = shipment.getOrder();
            order.changeStatus(OrderStatus.SHIPPED, "Shipment in transit", "SYSTEM");
        }
        
        // 배송 완료 상태로 변경 시
        if (status == ShipmentStatus.DELIVERED) {
            shipment.setActualDeliveryDate(LocalDateTime.now().toLocalDate());
            
            // 주문 상태도 배송 완료로 변경
            Order order = shipment.getOrder();
            order.changeStatus(OrderStatus.DELIVERED, "Shipment delivered", "SYSTEM");
        }
        
        return shipmentRepository.save(shipment);
    }
    
    @Transactional
    public Order cancelOrder(Long orderId, String reason, String cancelledBy) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        // 이미 배송중이거나 배송 완료된 주문은 취소 불가
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel order that has been shipped or delivered");
        }
        
        // 주문 상태 변경
        order.changeStatus(OrderStatus.CANCELLED, reason, cancelledBy);
        
        // 재고 복구
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            product.addStock(item.getQuantity());
            productRepository.save(product);
        }
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        // 배송 완료 상태인 주문만 완료 처리 가능
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("Only delivered orders can be marked as completed");
        }
        
        // 주문 상태 변경
        order.changeStatus(OrderStatus.COMPLETED, "Order completed", "SYSTEM");
        
        return orderRepository.save(order);
    }
    
    // 주문 상태 이력 조회
    public List<OrderStatusHistory> getOrderStatusHistory(Long orderId) {
        return orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(orderId);
    }
    
    // 주문 통계
    public Long countOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.countOrdersBetweenDates(startDate, endDate);
    }
    
    public BigDecimal sumSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.sumFinalAmountBetweenDates(startDate, endDate);
    }
    
    @Transactional
    public Order createOrder(Order order) {
        // 필요한 검증/비즈니스 로직이 있다면 추가
        return orderRepository.save(order);
    }
}