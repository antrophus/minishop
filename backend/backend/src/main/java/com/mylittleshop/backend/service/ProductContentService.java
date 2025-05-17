package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductContent;
import com.mylittleshop.backend.repository.ProductContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductContentService {
    private final ProductContentRepository productContentRepository;

    public ProductContent save(ProductContent content) { return productContentRepository.save(content); }
    public Optional<ProductContent> findById(Long id) { return productContentRepository.findById(id); }
    public List<ProductContent> findAll() { return productContentRepository.findAll(); }
    public void deleteById(Long id) { productContentRepository.deleteById(id); }
} 