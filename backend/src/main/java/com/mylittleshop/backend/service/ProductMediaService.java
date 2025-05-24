package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductMedia;
import com.mylittleshop.backend.repository.ProductMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductMediaService {
    private final ProductMediaRepository productMediaRepository;

    public ProductMedia save(ProductMedia media) { return productMediaRepository.save(media); }
    public Optional<ProductMedia> findById(Long id) { return productMediaRepository.findById(id); }
    public List<ProductMedia> findAll() { return productMediaRepository.findAll(); }
    public void deleteById(Long id) { productMediaRepository.deleteById(id); }
} 