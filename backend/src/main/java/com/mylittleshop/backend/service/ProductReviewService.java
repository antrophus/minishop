package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductReview;
import com.mylittleshop.backend.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductReviewRepository productReviewRepository;

    public ProductReview save(ProductReview review) { return productReviewRepository.save(review); }
    public Optional<ProductReview> findById(Long id) { return productReviewRepository.findById(id); }
    public List<ProductReview> findAll() { return productReviewRepository.findAll(); }
    public void deleteById(Long id) { productReviewRepository.deleteById(id); }
} 