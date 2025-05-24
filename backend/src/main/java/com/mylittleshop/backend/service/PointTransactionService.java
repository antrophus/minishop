package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.PointTransaction;
import com.mylittleshop.backend.repository.PointTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointTransactionService {
    private final PointTransactionRepository pointTransactionRepository;

    @Autowired
    public PointTransactionService(PointTransactionRepository pointTransactionRepository) {
        this.pointTransactionRepository = pointTransactionRepository;
    }

    public List<PointTransaction> findAll() {
        return pointTransactionRepository.findAll();
    }

    public PointTransaction save(PointTransaction pointTransaction) {
        return pointTransactionRepository.save(pointTransaction);
    }

    // 필요에 따라 추가 메서드 구현
} 