package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.*;
import com.mylittleshop.backend.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanService subscriptionPlanService;
    private final SubscriptionPaymentService subscriptionPaymentService;
    private final RecurringOrderService recurringOrderService;

    /**
     * 구독 생성
     * 
     * @param subscription 구독 객체
     * @return 저장된 구독
     */
    @Transactional
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    /**
     * ID로 구독 조회
     * 
     * @param id 구독 ID
     * @return 구독 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    /**
     * 모든 구독 조회
     * 
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    /**
     * 구독 삭제
     * 
     * @param id 구독 ID
     */
    @Transactional
    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }
    
    /**
     * 사용자별 구독 조회
     * 
     * @param user 사용자
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }
    
    /**
     * 사용자 ID별 구독 조회
     * 
     * @param userId 사용자 ID
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }
    
    /**
     * 상태별 구독 조회
     * 
     * @param status 구독 상태
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findByStatus(SubscriptionStatus status) {
        return subscriptionRepository.findByStatus(status);
    }
    
    /**
     * 구독 플랜별 구독 조회
     * 
     * @param plan 구독 플랜
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findByPlan(SubscriptionPlan plan) {
        return subscriptionRepository.findByPlan(plan);
    }
    
    /**
     * 구독 플랜 ID별 구독 조회
     * 
     * @param planId 구독 플랜 ID
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findByPlanId(Long planId) {
        return subscriptionRepository.findByPlanId(planId);
    }
    
    /**
     * 특정 날짜 이후에 시작된 구독 조회
     * 
     * @param date 기준 날짜
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findByContractStartDateAfter(LocalDate date) {
        return subscriptionRepository.findByContractStartDateAfter(date);
    }
    
    /**
     * 특정 날짜 이전에 종료된 구독 조회
     * 
     * @param date 기준 날짜
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findByContractEndDateBefore(LocalDate date) {
        return subscriptionRepository.findByContractEndDateBefore(date);
    }
    
    /**
     * 특정 날짜 범위 내에 활성화된 구독 조회
     * 
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 구독 목록
     */
    @Transactional(readOnly = true)
    public List<Subscription> findActiveSubscriptionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.findActiveSubscriptionsBetweenDates(startDate, endDate);
    }
    
    /**
     * 새 구독 생성
     * 
     * @param user 사용자
     * @param planId 구독 플랜 ID
     * @param startDate 시작 날짜
     * @param paymentMethodId 결제 수단 ID
     * @return 생성된 구독
     */
    @Transactional
    public Subscription createSubscription(User user, Long planId, LocalDate startDate, Long paymentMethodId) {
        Optional<SubscriptionPlan> planOpt = subscriptionPlanService.findById(planId);
        if (!planOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 플랜 ID입니다.");
        }
        
        SubscriptionPlan plan = planOpt.get();
        
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setContractStartDate(startDate);
        
        // 구독 기간 계산
        if (plan.getDuration() != null && plan.getDuration() > 0) {
            subscription.setContractEndDate(startDate.plusMonths(plan.getDuration()));
        }
        
        subscription.setNextBillingDate(startDate.plusMonths(1)); // 첫 결제 후 다음 결제일은
        subscription.setPaymentMethod(paymentMethodId.toString());
        
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        
        // 첫 결제 처리
        processSubscriptionPayment(savedSubscription);
                       
        return savedSubscription;
    }
    
    /**
     * 구독 갱신
     * 
     * @param subscriptionId 구독 ID
     * @return 갱신된 구독
     */
    @Transactional
    public Subscription renewSubscription(Long subscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 ID입니다.");
        }
        
        Subscription subscription = subscriptionOpt.get();
        
        // 갱신 가능 여부 확인
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE && 
            subscription.getStatus() != SubscriptionStatus.EXPIRED) {
            throw new IllegalStateException("활성 또는 만료 예정 상태의 구독만 갱신할 수 있습니다.");
        }
        
        // 구독 기간 연장
        LocalDate currentEndDate = subscription.getContractEndDate();
        if (currentEndDate == null) {
            currentEndDate = LocalDate.now();
        }
        
        SubscriptionPlan plan = subscription.getPlan();
        if (plan.getDuration() != null && plan.getDuration() > 0) {
            subscription.setContractEndDate(currentEndDate.plusMonths(plan.getDuration()));
        }
        
        // 다음 결제일 업데이트
        subscription.setNextBillingDate(LocalDate.now().plusMonths(1));
        
        // 상태 업데이트
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        
        // 결제 처리
        processSubscriptionPayment(subscription);
        
        return subscriptionRepository.save(subscription);
    }
    
    /**
     * 구독 취소
     * 
     * @param subscriptionId 구독 ID
     * @param cancelReason 취소 사유
     * @return 취소된 구독
     */
    @Transactional
    public Subscription cancelSubscription(Long subscriptionId, String cancelReason) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 ID입니다.");
        }
        
        Subscription subscription = subscriptionOpt.get();
        
        // 이미 취소된 경우
        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            return subscription;
        }
        
        // 상태 업데이트
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancellationReason(cancelReason);
        subscription.setCancellationDate(LocalDateTime.now());
        
        // 연관된 정기 주문 취소
        cancelRelatedRecurringOrders(subscription);
        
        return subscriptionRepository.save(subscription);
    }
    
    /**
     * 구독 일시 중지
     * 
     * @param subscriptionId 구독 ID
     * @param pauseReason 중지 사유
     * @return 일시 중지된 구독
     */
    @Transactional
    public Subscription pauseSubscription(Long subscriptionId, String pauseReason) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 ID입니다.");
        }
        
        Subscription subscription = subscriptionOpt.get();
        
        // 이미 일시 중지된 경우
        if (subscription.getStatus() == SubscriptionStatus.PAUSED) {
            return subscription;
        }
        
        // 일시 중지할 수 없는 상태인 경우
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("활성 상태의 구독만 일시 중지할 수 있습니다.");
        }
        
        // 상태 업데이트
        subscription.setStatus(SubscriptionStatus.PAUSED);
        subscription.setPauseReason(pauseReason);
        subscription.setPauseStartDate(LocalDateTime.now());
        
        // 연관된 정기 주문 일시 중지
        pauseRelatedRecurringOrders(subscription);
        
        return subscriptionRepository.save(subscription);
    }
    
    /**
     * 구독 재개
     * 
     * @param subscriptionId 구독 ID
     * @return 재개된 구독
     */
    @Transactional
    public Subscription resumeSubscription(Long subscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 ID입니다.");
        }
        
        Subscription subscription = subscriptionOpt.get();
        
        // 일시 중지 상태가 아닌 경우
        if (subscription.getStatus() != SubscriptionStatus.PAUSED) {
            throw new IllegalStateException("일시 중지 상태의 구독만 재개할 수 있습니다.");
        }
        
        // 상태 업데이트
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setPauseEndDate(LocalDateTime.now());
        
        // 일시 중지 기간만큼 종료일 및 다음 결제일 연장
        if (subscription.getPauseStartDate() != null) {
            long pauseDays = subscription.getPauseStartDate().toLocalDate().until(LocalDate.now()).getDays();
            
            if (subscription.getContractEndDate() != null) {
                subscription.setContractEndDate(subscription.getContractEndDate().plusDays(pauseDays));
            }
            
            if (subscription.getNextBillingDate() != null) {
                subscription.setNextBillingDate(subscription.getNextBillingDate().plusDays(pauseDays));
            }
        }
        
        // 연관된 정기 주문 재개
        resumeRelatedRecurringOrders(subscription);
        
        return subscriptionRepository.save(subscription);
    }
    
    /**
     * 구독 플랜 변경
     * 
     * @param subscriptionId 구독 ID
     * @param newPlanId 새 구독 플랜 ID
     * @return 변경된 구독
     */
    @Transactional
    public Subscription changeSubscriptionPlan(Long subscriptionId, Long newPlanId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 ID입니다.");
        }
        
        Optional<SubscriptionPlan> planOpt = subscriptionPlanService.findById(newPlanId);
        if (!planOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 플랜 ID입니다.");
        }
        
        Subscription subscription = subscriptionOpt.get();
        SubscriptionPlan newPlan = planOpt.get();
        
        // 플랜 변경
        subscription.setPlan(newPlan);
        
        // 필요한 경우 종료일 재계산
        if (newPlan.getDuration() != null && newPlan.getDuration() > 0) {
            LocalDate startDate = subscription.getContractStartDate();
            if (startDate == null) {
                startDate = LocalDate.now();
            }
            subscription.setContractEndDate(startDate.plusMonths(newPlan.getDuration()));
        }
        
        return subscriptionRepository.save(subscription);
    }
    
    /**
     * 구독 상태 확인 및 업데이트
     * 
     * @param subscriptionId 구독 ID
     * @return 업데이트된 구독
     */
    @Transactional
    public Subscription checkAndUpdateSubscriptionStatus(Long subscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionOpt.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 구독 ID입니다.");
        }
        
        Subscription subscription = subscriptionOpt.get();
        
        // 활성 상태가 아니면 상태 변경 없음
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            return subscription;
        }
        
        LocalDate today = LocalDate.now();
        
        // 종료일이 지난 경우
        if (subscription.getContractEndDate() != null && subscription.getContractEndDate().isBefore(today)) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            return subscriptionRepository.save(subscription);
        }
        
        // 종료일이 30일 이내인 경우
        if (subscription.getContractEndDate() != null && 
            subscription.getContractEndDate().isAfter(today) && 
            subscription.getContractEndDate().isBefore(today.plusDays(30))) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            return subscriptionRepository.save(subscription);
        }
        
        // 다음 결제일이 도래한 경우 결제 처리
        if (subscription.getNextBillingDate() != null && 
            subscription.getNextBillingDate().isBefore(today) || 
            subscription.getNextBillingDate().isEqual(today)) {
            processSubscriptionPayment(subscription);
            
            // 다음 결제일 업데이트
            subscription.setNextBillingDate(today.plusMonths(1));
            return subscriptionRepository.save(subscription);
        }
        
        return subscription;
    }
    
    /**
     * 모든 활성 구독의 상태 일괄 확인 및 업데이트
     */
    @Transactional
    public void checkAndUpdateAllSubscriptionsStatus() {
        List<Subscription> activeSubscriptions = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);
        for (Subscription subscription : activeSubscriptions) {
            try {
                checkAndUpdateSubscriptionStatus(subscription.getId());
            } catch (Exception e) {
                // 로깅 및 예외 처리
            }
        }
    }
    
    /**
     * 구독 결제 처리
     * 
     * @param subscription 구독
     * @return 처리된 결제
     */
    @Transactional
    public SubscriptionPayment processSubscriptionPayment(Subscription subscription) {
        SubscriptionPlan plan = subscription.getPlan();
        
        SubscriptionPayment payment = new SubscriptionPayment();
        payment.setSubscription(subscription);
        payment.setAmount(plan.getPrice());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentMethod(subscription.getPaymentMethod());
        
        return subscriptionPaymentService.save(payment);
    }
    
    /**
     * 구독에 대한 정기 주문 생성
     * 
     * @param subscription 구독
     * @return 생성된 정기 주문
     */
    @Transactional
    public RecurringOrder createRecurringOrderForSubscription(Subscription subscription) {
        // SubscriptionPlan plan = subscription.getPlan();
        User user = subscription.getUser();
        
        // 기본 정기 주문 생성
        return recurringOrderService.createRecurringOrder(
                user, 
                subscription, 
                "MONTHLY", // 구독 주기에 따라 변경 가능
                LocalDate.now().plusMonths(1), // 다음 주문일
                null, // 배송지 ID는 필요에 따라 설정
                Long.valueOf(subscription.getPaymentMethod()) // String → Long 변환
        );
    }
    
    /**
     * 구독 관련 정기 주문 취소
     * 
     * @param subscription 구독
     */
    @Transactional
    public void cancelRelatedRecurringOrders(Subscription subscription) {
        List<RecurringOrder> recurringOrders = recurringOrderService.getRecurringOrdersBySubscription(subscription);
        for (RecurringOrder order : recurringOrders) {
            recurringOrderService.cancelRecurringOrder(order.getId());
        }
    }
    
    /**
     * 구독 관련 정기 주문 일시 중지
     * 
     * @param subscription 구독
     */
    @Transactional
    public void pauseRelatedRecurringOrders(Subscription subscription) {
        List<RecurringOrder> recurringOrders = recurringOrderService.getRecurringOrdersBySubscription(subscription);
        for (RecurringOrder order : recurringOrders) {
            recurringOrderService.pauseRecurringOrder(order.getId());
        }
    }
    
    /**
     * 구독 관련 정기 주문 재개
     * 
     * @param subscription 구독
     */
    @Transactional
    public void resumeRelatedRecurringOrders(Subscription subscription) {
        List<RecurringOrder> recurringOrders = recurringOrderService.getRecurringOrdersBySubscription(subscription);
        for (RecurringOrder order : recurringOrders) {
            recurringOrderService.resumeRecurringOrder(order.getId());
        }
    }
    
    /**
     * 페이징으로 구독 조회
     * 
     * @param pageable 페이징 정보
     * @return 구독 페이지
     */
    @Transactional(readOnly = true)
    public Page<Subscription> findAll(Pageable pageable) {
        return subscriptionRepository.findAll(pageable);
    }
    
    /**
     * 사용자별 구독 페이징 조회
     * 
     * @param user 사용자
     * @param pageable 페이징 정보
     * @return 구독 페이지
     */
    @Transactional(readOnly = true)
    public Page<Subscription> findByUser(User user, Pageable pageable) {
        return subscriptionRepository.findByUser(user, pageable);
    }
    
    /**
     * 사용자 ID별 구독 페이징 조회
     * 
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 구독 페이지
     */
    @Transactional(readOnly = true)
    public Page<Subscription> findByUserId(Long userId, Pageable pageable) {
        return subscriptionRepository.findByUserId(userId, pageable);
    }
}