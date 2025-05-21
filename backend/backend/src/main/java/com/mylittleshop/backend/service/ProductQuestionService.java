package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductQuestion;
import com.mylittleshop.backend.repository.ProductQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQuestionService {
    private final ProductQuestionRepository productQuestionRepository;

    @Autowired
    public ProductQuestionService(ProductQuestionRepository productQuestionRepository) {
        this.productQuestionRepository = productQuestionRepository;
    }

    public List<ProductQuestion> findAll() {
        return productQuestionRepository.findAll();
    }

    public ProductQuestion save(ProductQuestion productQuestion) {
        return productQuestionRepository.save(productQuestion);
    }

    // 필요에 따라 추가 메서드 구현
} 