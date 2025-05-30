package com.mylittleshop.backend.controller;

import com.mylittleshop.backend.model.ProductCategory;
import com.mylittleshop.backend.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 상품 카테고리 관련 API를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "상품 카테고리", description = "상품 카테고리 관련 API")
public class CategoryController {
    
    private final ProductCategoryService categoryService;

    /**
     * 모든 카테고리 조회
     */
    @Operation(summary = "모든 카테고리 조회", description = "모든 상품 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        try {
            List<ProductCategory> categories = categoryService.findAll();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "categories", categories.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList())
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "카테고리 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 특정 카테고리 조회
     */
    @Operation(summary = "특정 카테고리 조회", description = "ID로 특정 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategory(@PathVariable Long id) {
        try {
            Optional<ProductCategory> categoryOpt = categoryService.findById(id);
            
            if (categoryOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "카테고리를 찾을 수 없습니다."));
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", convertToDetailDTO(categoryOpt.get())
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "카테고리 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
    /**
     * 최상위 카테고리 조회 (부모가 없는 카테고리)
     */
    @Operation(summary = "최상위 카테고리 조회")
    @GetMapping("/root")
    public ResponseEntity<Map<String, Object>> getRootCategories() {
        try {
            List<ProductCategory> categories = categoryService.findAll().stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "categories", categories.stream()
                        .map(this::convertToTreeDTO)
                        .collect(Collectors.toList())
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "최상위 카테고리 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // Private helper methods
    
    /**
     * ProductCategory 엔티티를 DTO로 변환
     */
    private Map<String, Object> convertToDTO(ProductCategory category) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", category.getId());
        dto.put("name", category.getName());
        
        if (category.getParent() != null) {
            dto.put("parentId", category.getParent().getId());
            dto.put("parentName", category.getParent().getName());
        }
        
        return dto;
    }
    
    /**
     * ProductCategory 엔티티를 상세 DTO로 변환
     */
    private Map<String, Object> convertToDetailDTO(ProductCategory category) {
        Map<String, Object> dto = convertToDTO(category);
        
        // 하위 카테고리들
        if (!category.getChildren().isEmpty()) {
            dto.put("children", category.getChildren().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        }
        
        // 상품 수
        dto.put("productCount", category.getProducts().size());
        
        return dto;
    }
    
    /**
     * ProductCategory 엔티티를 트리 구조 DTO로 변환
     */
    private Map<String, Object> convertToTreeDTO(ProductCategory category) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", category.getId());
        dto.put("name", category.getName());
        dto.put("productCount", category.getProducts().size());
        
        // 하위 카테고리들을 재귀적으로 포함
        if (!category.getChildren().isEmpty()) {
            dto.put("children", category.getChildren().stream()
                .map(this::convertToTreeDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}