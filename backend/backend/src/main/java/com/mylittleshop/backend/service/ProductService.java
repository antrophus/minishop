package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product save(Product product) { return productRepository.save(product); }
    public Optional<Product> findById(Long id) { return productRepository.findById(id); }
    public List<Product> findAll() { return productRepository.findAll(); }
    public void deleteById(Long id) { productRepository.deleteById(id); }
} 