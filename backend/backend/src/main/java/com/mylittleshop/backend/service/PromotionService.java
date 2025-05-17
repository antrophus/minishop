package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Promotion;
import com.mylittleshop.backend.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public Promotion save(Promotion promotion) { return promotionRepository.save(promotion); }
    public Optional<Promotion> findById(Long id) { return promotionRepository.findById(id); }
    public List<Promotion> findAll() { return promotionRepository.findAll(); }
    public void deleteById(Long id) { promotionRepository.deleteById(id); }
} 