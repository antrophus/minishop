package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.SubscriptionPayment;
import com.mylittleshop.backend.repository.SubscriptionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPaymentService {
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;

    public SubscriptionPayment save(SubscriptionPayment payment) {
        return subscriptionPaymentRepository.save(payment);
    }

    public Optional<SubscriptionPayment> findById(Long id) {
        return subscriptionPaymentRepository.findById(id);
    }

    public List<SubscriptionPayment> findAll() {
        return subscriptionPaymentRepository.findAll();
    }

    public void deleteById(Long id) {
        subscriptionPaymentRepository.deleteById(id);
    }
} 