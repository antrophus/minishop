package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ReviewResponse;
import com.mylittleshop.backend.repository.ReviewResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewResponseService {
    private final ReviewResponseRepository reviewResponseRepository;

    @Autowired
    public ReviewResponseService(ReviewResponseRepository reviewResponseRepository) {
        this.reviewResponseRepository = reviewResponseRepository;
    }

    public List<ReviewResponse> findAll() {
        return reviewResponseRepository.findAll();
    }

    public ReviewResponse save(ReviewResponse reviewResponse) {
        return reviewResponseRepository.save(reviewResponse);
    }
    

    // 필요에 따라 추가 메서드 구현
} 