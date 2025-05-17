package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.ProductAttribute;
import com.mylittleshop.backend.repository.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;

    public ProductAttribute save(ProductAttribute attribute) { return productAttributeRepository.save(attribute); }
    public Optional<ProductAttribute> findById(Long id) { return productAttributeRepository.findById(id); }
    public List<ProductAttribute> findAll() { return productAttributeRepository.findAll(); }
    public void deleteById(Long id) { productAttributeRepository.deleteById(id); }
} 