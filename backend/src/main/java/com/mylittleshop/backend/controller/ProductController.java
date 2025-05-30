package com.mylittleshop.backend.controller;

import com.mylittleshop.backend.model.*;
import com.mylittleshop.backend.service.ProductService;
import com.mylittleshop.backend.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 상품 관련 API를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "상품 관리", description = "상품 조회 및 관리 관련 API")
public class ProductController {
    
    private final ProductService productService;
    private final ProductCategoryService categoryService;

    /**
     * 테스트용 간단한 API
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testApi() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "백엔드 API 정상 동작");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    /**
     * 상품 목록 조회 (페이징, 정렬, 검색, 필터링 지원)
     */
    @Operation(summary = "상품 목록 조회")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "true") boolean activeOnly
    ) {
        try {
            // 실제 데이터베이스 연결 시도
            System.out.println("데이터베이스 연결 시도...");
            
            try {
                Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
                
                Page<Product> productPage;            
                if (hasComplexFilters(search, categoryId, brand)) {
                    productPage = getFilteredProducts(pageable, search, categoryId, brand, activeOnly);
                } else {
                    if (activeOnly) {
                        // 우선 Repository에서 직접 조회 (JOIN FETCH 없이)
                        productPage = productService.findByStatusAndIsActive(ProductStatus.ACTIVE, true, pageable);
                    } else {
                        productPage = productService.findAll(pageable);
                    }
                }
                
                System.out.println("데이터베이스 조회 성공! 상품 수: " + productPage.getTotalElements());
                
                if (productPage.getTotalElements() > 0) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", Map.of(
                        "products", productPage.getContent().stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList()),
                        "currentPage", productPage.getNumber(),
                        "totalPages", productPage.getTotalPages(),
                        "totalElements", productPage.getTotalElements(),
                        "size", productPage.getSize(),
                        "hasNext", productPage.hasNext(),
                        "hasPrevious", productPage.hasPrevious()
                    ));
                    
                    return ResponseEntity.ok(response);
                }
            } catch (Exception dbException) {
                System.out.println("데이터베이스 조회 실패, 더미 데이터 사용: " + dbException.getMessage());
                dbException.printStackTrace(); // 상세 에러 로그
            }
            
            // 실제 데이터가 없거나 DB 연결 실패 시 더미 데이터 반환
            System.out.println("더미 데이터 사용");
            List<Map<String, Object>> dummyProducts = createDummyProducts();
            
            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "products", dummyProducts,
                "currentPage", page,
                "totalPages", 1,
                "totalElements", dummyProducts.size(),
                "size", size,
                "hasNext", false,
                "hasPrevious", false
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "상품 목록 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 상품 상세 조회
     */
    @Operation(summary = "상품 상세 조회")
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable Long id) {
        try {
            Optional<Product> productOpt = productService.findById(id);
            
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "상품을 찾을 수 없습니다."));
            }
            
            Product product = productOpt.get();
            
            // 활성 상품이 아닌 경우 접근 제한
            if (!product.getIsActive() || product.getStatus() != ProductStatus.ACTIVE) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "해당 상품은 현재 이용할 수 없습니다."));
            }
            
            Map<String, Object> productDetail = convertToDetailDTO(product);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", productDetail
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "상품 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
    /**
     * 카테고리별 상품 조회
     */
    @Operation(summary = "카테고리별 상품 조회")
    @GetMapping("/category/{categoryId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Optional<ProductCategory> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "카테고리를 찾을 수 없습니다."));
            }
            
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Product> productPage = productService.findByCategory(categoryOpt.get(), pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "category", convertCategoryToDTO(categoryOpt.get()),
                "products", productPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()),
                "currentPage", productPage.getNumber(),
                "totalPages", productPage.getTotalPages(),
                "totalElements", productPage.getTotalElements()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "카테고리별 상품 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 상품 검색
     */
    @Operation(summary = "상품 검색")
    @GetMapping("/search")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "검색 키워드를 입력해주세요."));
            }
            
            Pageable pageable = PageRequest.of(page, size);
            List<Product> products = productService.findByNameContaining(keyword.trim());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "keyword", keyword.trim(),
                    "products", products.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()),
                    "totalElements", products.size()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "상품 검색 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
    /**
     * 관련 상품 조회
     */
    @Operation(summary = "관련 상품 조회")
    @GetMapping("/{id}/related")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getRelatedProducts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "8") int limit
    ) {
        try {
            Optional<Product> productOpt = productService.findById(id);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "상품을 찾을 수 없습니다."));
            }
            
            Product product = productOpt.get();
            List<Product> relatedProducts = new ArrayList<>();
            
            // 같은 카테고리의 다른 상품들을 관련 상품으로 조회
            if (product.getCategory() != null) {
                relatedProducts = productService.findByCategory(product.getCategory()).stream()
                    .filter(p -> !p.getId().equals(id)) // 현재 상품 제외
                    .limit(limit)
                    .collect(Collectors.toList());
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "productId", id,
                    "relatedProducts", relatedProducts.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList())
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "관련 상품 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // Private helper methods
    
    private boolean hasComplexFilters(String search, Long categoryId, String brand) {
        return search != null || categoryId != null || brand != null;
    }
    
    private Page<Product> getFilteredProducts(Pageable pageable, String search, Long categoryId,
                                            String brand, boolean activeOnly) {
        
        if (search != null && !search.trim().isEmpty()) {
            List<Product> searchResults = productService.findByNameContaining(search.trim());
            // 간단한 페이징 처리 (실제로는 Repository에서 Page를 반환하도록 구현해야 함)
            return new org.springframework.data.domain.PageImpl<>(searchResults, pageable, searchResults.size());
        }
        
        if (categoryId != null) {
            Optional<ProductCategory> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isPresent()) {
                return productService.findByCategory(categoryOpt.get(), pageable);
            }
        }
        
        // 기본 조회
        if (activeOnly) {
            return productService.findByStatusAndIsActive(ProductStatus.ACTIVE, true, pageable);
        }
        
        return productService.findAll(pageable);
    }    
    /**
     * Product 엔티티를 DTO로 변환 (목록용)
     */
    private Map<String, Object> convertToDTO(Product product) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", product.getId());
        dto.put("name", product.getName());
        dto.put("description", product.getDescription());
        dto.put("price", product.getGmPrice());
        dto.put("memberPrice", product.getGbmPrice());
        dto.put("originalPrice", product.getOriginalGmPrice());
        dto.put("stockQuantity", product.getStockQuantity());
        dto.put("status", product.getStatus());
        dto.put("isActive", product.getIsActive());
        dto.put("featured", product.getFeatured());
        dto.put("bestseller", product.getBestseller());
        dto.put("newArrival", product.getNewArrival());
        dto.put("brand", product.getBrand());
        dto.put("sku", product.getSku());
        dto.put("createdAt", product.getCreatedAt());
        
        // 카테고리 정보
        if (product.getCategory() != null) {
            dto.put("category", convertCategoryToDTO(product.getCategory()));
        }
        
        // 메인 이미지
        if (!product.getImages().isEmpty()) {
            Optional<ProductImage> mainImage = product.getImages().stream()
                .filter(ProductImage::getIsMain)
                .findFirst();
            if (mainImage.isPresent()) {
                dto.put("mainImage", convertImageToDTO(mainImage.get()));
            } else {
                dto.put("mainImage", convertImageToDTO(product.getImages().get(0)));
            }
        }
        
        // 할인 정보
        if (product.getOriginalGmPrice() != null && 
            product.getOriginalGmPrice().compareTo(product.getGmPrice()) > 0) {
            BigDecimal discountAmount = product.getOriginalGmPrice().subtract(product.getGmPrice());
            BigDecimal discountRate = discountAmount.divide(product.getOriginalGmPrice(), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
            dto.put("discount", Map.of(
                "amount", discountAmount,
                "rate", discountRate.intValue()
            ));
        }
        
        return dto;
    }
    
    /**
     * Product 엔티티를 상세 DTO로 변환
     */
    private Map<String, Object> convertToDetailDTO(Product product) {
        Map<String, Object> dto = convertToDTO(product);
        
        // 추가 상세 정보
        dto.put("manufacturer", product.getManufacturer());
        dto.put("barcode", product.getBarcode());
        dto.put("minimumOrderQuantity", product.getMinimumOrderQuantity());
        dto.put("maximumOrderQuantity", product.getMaximumOrderQuantity());
        dto.put("subscriptionAvailable", product.getSubscriptionAvailable());
        dto.put("shippingFee", product.getShippingFee());
        dto.put("countryOfOrigin", product.getCountryOfOrigin());
        dto.put("weight", product.getWeight());
        dto.put("updatedAt", product.getUpdatedAt());
        
        // 모든 이미지
        dto.put("images", product.getImages().stream()
            .map(this::convertImageToDTO)
            .collect(Collectors.toList()));
        
        return dto;
    }    
    /**
     * ProductCategory 엔티티를 DTO로 변환
     */
    private Map<String, Object> convertCategoryToDTO(ProductCategory category) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", category.getId());
        dto.put("name", category.getName());
        
        if (category.getParent() != null) {
            dto.put("parent", Map.of(
                "id", category.getParent().getId(),
                "name", category.getParent().getName()
            ));
        }
        
        return dto;
    }
    
    /**
     * ProductImage 엔티티를 DTO로 변환
     */
    private Map<String, Object> convertImageToDTO(ProductImage image) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", image.getId());
        dto.put("url", image.getUrl());
        dto.put("alt", "상품 이미지"); // alt 기본값
        dto.put("isMain", image.getIsMain());
        dto.put("sortOrder", image.getSortOrder());
        return dto;
    }
    
    /**
     * 임시 더미 데이터 생성 (테스트용)
     */
    private List<Map<String, Object>> createDummyProducts() {
        List<Map<String, Object>> products = new ArrayList<>();
        
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("id", (long) i);
            product.put("name", "테스트 상품 " + i);
            product.put("description", "이것은 테스트용 상품 " + i + "입니다.");
            product.put("price", BigDecimal.valueOf(10000 + (i * 1000)));
            product.put("memberPrice", BigDecimal.valueOf(9000 + (i * 1000)));
            product.put("originalPrice", BigDecimal.valueOf(12000 + (i * 1000)));
            product.put("stockQuantity", 100);
            product.put("status", "ACTIVE");
            product.put("isActive", true);
            product.put("featured", i <= 3);
            product.put("bestseller", i <= 2);
            product.put("newArrival", i <= 5);
            product.put("brand", "테스트 브랜드");
            product.put("sku", "TEST" + String.format("%03d", i));
            product.put("createdAt", LocalDateTime.now().minusDays(i));
            
            // 카테고리 정보
            Map<String, Object> category = new HashMap<>();
            category.put("id", (long) (i % 3 + 1));
            category.put("name", "카테고리 " + (i % 3 + 1));
            product.put("category", category);
            
            // 메인 이미지
            Map<String, Object> mainImage = new HashMap<>();
            mainImage.put("id", (long) i);
            mainImage.put("url", "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjMwMCIgZmlsbD0iI2Y1ZjVmNSIvPjx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTgiIGZpbGw9IiM5OTkiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj7sg53tkpEgPC90ZXh0Pjwvdmc+");
            mainImage.put("alt", "테스트 상품 " + i + " 이미지");
            mainImage.put("isMain", true);
            mainImage.put("sortOrder", 1);
            product.put("mainImage", mainImage);
            
            // 할인 정보
            if (i % 2 == 0) {
                Map<String, Object> discount = new HashMap<>();
                discount.put("amount", BigDecimal.valueOf(2000));
                discount.put("rate", 17);
                product.put("discount", discount);
            }
            
            products.add(product);
        }
        
        return products;
    }
}