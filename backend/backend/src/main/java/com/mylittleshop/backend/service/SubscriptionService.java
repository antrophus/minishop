package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Subscription;
import com.mylittleshop.backend.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }
} 