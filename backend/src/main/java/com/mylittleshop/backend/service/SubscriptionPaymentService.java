package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Subscription;
import com.mylittleshop.backend.model.SubscriptionPayment;
import com.mylittleshop.backend.model.PaymentStatus;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.repository.SubscriptionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPaymentService {
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;

    /**
     * 구독 결제 저장
     * 
     * @param payment 구독 결제
     * @return 저장된 구독 결제
     */
    @Transactional
    public SubscriptionPayment save(SubscriptionPayment payment) {
        return subscriptionPaymentRepository.save(payment);
    }

    /**
     * ID로 구독 결제 조회
     * 
     * @param id 구독 결제 ID
     * @return 구독 결제 Optional
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionPayment> findById(Long id) {
        return subscriptionPaymentRepository.findById(id);
    }

    /**
     * 모든 구독 결제 조회
     * 
     * @return 구독 결제 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPayment> findAll() {
        return subscriptionPaymentRepository.findAll();
    }

    /**
     * 구독 결제 삭제
     * 
     * @param id 구독 결제 ID
     */
    @Transactional
    public void deleteById(Long id) {
        subscriptionPaymentRepository.deleteById(id);
    }
    
    /**
     * 구독별 결제 조회
     * 
     * @param subscription 구독
     * @return 구독 결제 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPayment> findBySubscription(Subscription subscription) {
        return subscriptionPaymentRepository.findBySubscription(subscription);
    }
    
    /**
     * 구독 ID별 결제 조회
     * 
     * @param subscriptionId 구독 ID
     * @return 구독 결제 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPayment> findBySubscriptionId(Long subscriptionId) {
        return subscriptionPaymentRepository.findBySubscriptionId(subscriptionId);
    }
    
    /**
     * 구독별 결제 페이징 조회
     * 
     * @param subscription 구독
     * @param pageable 페이징 정보
     * @return 구독 결제 페이지
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionPayment> findBySubscription(Subscription subscription, Pageable pageable) {
        return subscriptionPaymentRepository.findBySubscription(subscription, pageable);
    }
    
    /**
     * 구독 ID별 결제 페이징 조회
     * 
     * @param subscriptionId 구독 ID
     * @param pageable 페이징 정보
     * @return 구독 결제 페이지
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionPayment> findBySubscriptionId(Long subscriptionId, Pageable pageable) {
        return subscriptionPaymentRepository.findBySubscriptionId(subscriptionId, pageable);
    }
    
    /**
     * 사용자별 결제 조회
     * 
     * @param user 사용자
     * @return 구독 결제 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPayment> findByUser(User user) {
        return subscriptionPaymentRepository.findBySubscriptionUser(user);
    }
    
    /**
     * 사용자 ID별 결제 조회
     * 
     * @param userId 사용자 ID
     * @return 구독 결제 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPayment> findByUserId(Long userId) {
        return subscriptionPaymentRepository.findBySubscriptionUserId(userId);
    }
    
    /**
     * 결제 상태로 결제 조회
     * 
     * @param status 결제 상태
     * @return 결제 목록
     */
    public List<SubscriptionPayment> findByStatus(PaymentStatus status) {
        return subscriptionPaymentRepository.findByStatus(status);
    }
    
    /**
     * 결제일 범위로 결제 조회
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 구독 결제 목록
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPayment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return subscriptionPaymentRepository.findByPaymentDateBetween(startDate, endDate);
    }
    
    /**
     * 결제일 범위로 결제 페이징 조회
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @param pageable 페이징 정보
     * @return 구독 결제 페이지
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionPayment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return subscriptionPaymentRepository.findByPaymentDateBetween(startDate, endDate, pageable);
    }
    
    /**
     * 구독별 최근 결제 조회
     * 
     * @param subscription 구독
     * @return 최근 구독 결제
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionPayment> findLatestBySubscription(Subscription subscription) {
        return subscriptionPaymentRepository.findTopBySubscriptionOrderByPaymentDateDesc(subscription);
    }
    
    /**
     * 구독 ID별 최근 결제 조회
     * 
     * @param subscriptionId 구독 ID
     * @return 최근 구독 결제
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionPayment> findLatestBySubscriptionId(Long subscriptionId) {
        return subscriptionPaymentRepository.findTopBySubscriptionIdOrderByPaymentDateDesc(subscriptionId);
    }
    
    /**
     * 결제액 범위로 결제 조회
     * 
     * @param minAmount 최소 금액
     * @param maxAmount 최대 금액
     * @return 결제 목록
     */
    public List<SubscriptionPayment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount) {
        return subscriptionPaymentRepository.findByAmountBetween(minAmount, maxAmount);
    }
    
    /**
     * 결제 상태 업데이트
     * 
     * @param id 결제 ID
     * @param status 결제 상태
     * @return 업데이트된 결제
     */
    @Transactional
    public Optional<SubscriptionPayment> updatePaymentStatus(Long id, PaymentStatus status) {
        return subscriptionPaymentRepository.findById(id).map(payment -> {
            payment.setStatus(status);
            return subscriptionPaymentRepository.save(payment);
        });
    }
    
    /**
     * 결제 메모 업데이트
     * 
     * @param id 결제 ID
     * @param note 메모
     * @return 업데이트된 결제
     */
    @Transactional
    public Optional<SubscriptionPayment> updatePaymentNote(Long id, String note) {
        return subscriptionPaymentRepository.findById(id).map(payment -> {
            payment.setNote(note);
            return subscriptionPaymentRepository.save(payment);
        });
    }
    
    /**
     * 결제 생성
     * 
     * @param subscription 구독
     * @param amount 금액
     * @param paymentMethodId 결제 수단 ID
     * @return 생성된 결제
     */
    @Transactional
    public SubscriptionPayment createPayment(Subscription subscription, BigDecimal amount, Long paymentMethodId) {
        SubscriptionPayment payment = new SubscriptionPayment();
        payment.setSubscription(subscription);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentMethodId(paymentMethodId);
        return subscriptionPaymentRepository.save(payment);
    }
    
    /**
     * 실패한 결제 등록
     * 
     * @param subscription 구독
     * @param amount 금액
     * @param paymentMethodId 결제 수단 ID
     * @param failReason 실패 사유
     * @return 생성된 결제
     */
    @Transactional
    public SubscriptionPayment createFailedPayment(Subscription subscription, BigDecimal amount, Long paymentMethodId, String failReason) {
        SubscriptionPayment payment = new SubscriptionPayment();
        payment.setSubscription(subscription);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.FAILED);
        payment.setPaymentMethodId(paymentMethodId);
        payment.setFailureReason(failReason);
        return subscriptionPaymentRepository.save(payment);
    }
    
    /**
     * 사용자별 특정 기간 결제 통계
     * 
     * @param userId 사용자 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 총 결제액
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaymentsByUserIdForPeriod(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = subscriptionPaymentRepository.sumPaymentAmountByUserIdAndDateRange(userId, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * 특정 기간 결제 통계
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 총 결제액
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaymentsForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = subscriptionPaymentRepository.sumPaymentAmountByDateRange(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * 기간별 결제 건수
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 결제 건수
     */
    @Transactional(readOnly = true)
    public long getPaymentCountForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return subscriptionPaymentRepository.countByPaymentDateBetween(startDate, endDate);
    }
    
    /**
     * 일별 결제 통계
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 일별 결제 통계
     */
    @Transactional(readOnly = true)
    public List<Object[]> getDailyPaymentStatistics(LocalDate startDate, LocalDate endDate) {
        return subscriptionPaymentRepository.getDailyPaymentStatistics(
                startDate.atStartOfDay(), 
                endDate.plusDays(1).atStartOfDay());
    }
    
    /**
     * 월별 결제 통계
     * 
     * @param year 연도
     * @return 월별 결제 통계
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMonthlyPaymentStatistics(int year) {
        return subscriptionPaymentRepository.getMonthlyPaymentStatistics(year);
    }
    
    /**
     * 페이징으로 모든 결제 조회
     * 
     * @param pageable 페이징 정보
     * @return 결제 페이지
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionPayment> findAll(Pageable pageable) {
        return subscriptionPaymentRepository.findAll(pageable);
    }
}