package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.FAQ;
import com.mylittleshop.backend.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FAQService {
    private final FAQRepository faqRepository;

    @Autowired
    public FAQService(FAQRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FAQ> findAll() {
        return faqRepository.findAll();
    }

    public FAQ save(FAQ faq) {
        return faqRepository.save(faq);
    }

    // 필요에 따라 추가 메서드 구현
} 