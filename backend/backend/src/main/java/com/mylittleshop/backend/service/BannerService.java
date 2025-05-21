package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Banner;
import com.mylittleshop.backend.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {
    private final BannerRepository bannerRepository;

    @Autowired
    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }

    public Banner save(Banner banner) {
        return bannerRepository.save(banner);
    }

    // 필요에 따라 추가 메서드 구현
} 