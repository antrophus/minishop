package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.SubscriptionPlan;
import com.mylittleshop.backend.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlan save(SubscriptionPlan plan) {
        return subscriptionPlanRepository.save(plan);
    }

    public Optional<SubscriptionPlan> findById(Long id) {
        return subscriptionPlanRepository.findById(id);
    }

    public List<SubscriptionPlan> findAll() {
        return subscriptionPlanRepository.findAll();
    }

    public void deleteById(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }
} 