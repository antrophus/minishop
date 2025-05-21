package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductRating;
import com.mylittleshop.backend.repository.ProductRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRatingService {
    private final ProductRatingRepository productRatingRepository;

    @Autowired
    public ProductRatingService(ProductRatingRepository productRatingRepository) {
        this.productRatingRepository = productRatingRepository;
    }

    public List<ProductRating> findAll() {
        return productRatingRepository.findAll();
    }

    public ProductRating save(ProductRating productRating) {
        return productRatingRepository.save(productRating);
    }

    // 필요에 따라 추가 메서드 구현
} 