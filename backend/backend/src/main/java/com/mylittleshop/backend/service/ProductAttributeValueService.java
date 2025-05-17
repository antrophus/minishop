package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductAttributeValue;
import com.mylittleshop.backend.repository.ProductAttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductAttributeValueService {
    private final ProductAttributeValueRepository productAttributeValueRepository;

    public ProductAttributeValue save(ProductAttributeValue value) { return productAttributeValueRepository.save(value); }
    public Optional<ProductAttributeValue> findById(Long id) { return productAttributeValueRepository.findById(id); }
    public List<ProductAttributeValue> findAll() { return productAttributeValueRepository.findAll(); }
    public void deleteById(Long id) { productAttributeValueRepository.deleteById(id); }
} 