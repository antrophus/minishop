package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Payment;
import com.mylittleshop.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment save(Payment payment) { return paymentRepository.save(payment); }
    public Optional<Payment> findById(Long id) { return paymentRepository.findById(id); }
    public List<Payment> findAll() { return paymentRepository.findAll(); }
    public void deleteById(Long id) { paymentRepository.deleteById(id); }
} 