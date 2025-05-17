package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductCategory;
import com.mylittleshop.backend.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategory save(ProductCategory category) { return productCategoryRepository.save(category); }
    public Optional<ProductCategory> findById(Long id) { return productCategoryRepository.findById(id); }
    public List<ProductCategory> findAll() { return productCategoryRepository.findAll(); }
    public void deleteById(Long id) { productCategoryRepository.deleteById(id); }
} 