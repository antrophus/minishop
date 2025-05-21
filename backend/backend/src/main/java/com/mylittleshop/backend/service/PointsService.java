package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Points;
import com.mylittleshop.backend.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsService {
    private final PointsRepository pointsRepository;

    @Autowired
    public PointsService(PointsRepository pointsRepository) {
        this.pointsRepository = pointsRepository;
    }

    public List<Points> findAll() {
        return pointsRepository.findAll();
    }

    public Points save(Points points) {
        return pointsRepository.save(points);
    }

    // 필요에 따라 추가 메서드 구현
} 