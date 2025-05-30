package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.*;
import com.mylittleshop.backend.repository.ProductCategoryRepository;
import com.mylittleshop.backend.repository.ProductContentRepository;
import com.mylittleshop.backend.repository.ProductImageRepository;
import com.mylittleshop.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductContentRepository productContentRepository;
    private final ProductCategoryRepository productCategoryRepository;
    
    // 기본 CRUD 작업
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    
    // 고급 조회 기능
    public List<Product> findByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }
    
    public Page<Product> findByCategory(ProductCategory category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }
    
    public List<Product> findByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }
    
    public Page<Product> findByStatus(ProductStatus status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable);
    }
    
    public List<Product> findByIsActive(Boolean isActive) {
        return productRepository.findByIsActive(isActive);
    }
    
    public List<Product> findByStatusAndIsActive(ProductStatus status, Boolean isActive) {
        // JOIN FETCH를 사용하여 Lazy Loading 문제 해결
        return productRepository.findByStatusAndIsActiveWithCategoryAndImages(status, isActive);
    }
    
    public Page<Product> findByStatusAndIsActive(ProductStatus status, Boolean isActive, Pageable pageable) {
        return productRepository.findByStatusAndIsActive(status, isActive, pageable);
    }
    
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByGmPriceBetween(minPrice, maxPrice);
    }
    
    public List<Product> findByNameContaining(String namePart) {
        return productRepository.findByNameContaining(namePart);
    }
    
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    public List<Product> findFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }
    
    public List<Product> findBestsellerProducts() {
        return productRepository.findByBestsellerTrue();
    }
    
    public List<Product> findNewArrivals() {
        return productRepository.findByNewArrivalTrue();
    }
    
    public List<Product> findByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }
    
    public List<Product> findLowStockProducts(Integer threshold) {
        return productRepository.findByStockQuantityLessThan(threshold);
    }
    
    public List<Product> findSoldOutProducts() {
        return productRepository.findSoldOutProducts();
    }
    
    public List<Product> findSubscribableProducts() {
        return productRepository.findBySubscriptionAvailableTrue();
    }
    
    public List<Product> findDiscountedProducts() {
        return productRepository.findDiscountedProducts();
    }
    
    public List<Product> findFreeShippingProducts() {
        return productRepository.findByShippingFeeEquals(BigDecimal.ZERO);
    }
    
    // 비즈니스 로직
    @Transactional
    public Product createProduct(Product product, List<ProductImage> images, ProductContent content) {
        // 상품 저장
        Product savedProduct = productRepository.save(product);
        
        // 이미지 저장
        if (images != null && !images.isEmpty()) {
            for (ProductImage image : images) {
                image.setProduct(savedProduct);
                productImageRepository.save(image);
            }
        }
        
        // 상품 콘텐츠 저장
        if (content != null) {
            content.setProduct(savedProduct);
            productContentRepository.save(content);
        }
        
        return savedProduct;
    }
    
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // 기본 필드 업데이트
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setGmPrice(productDetails.getGmPrice());
                    existingProduct.setGbmPrice(productDetails.getGbmPrice());
                    existingProduct.setShopPrice(productDetails.getShopPrice());
                    existingProduct.setCostPrice(productDetails.getCostPrice());
                    existingProduct.setStockQuantity(productDetails.getStockQuantity());
                    existingProduct.setStatus(productDetails.getStatus());
                    existingProduct.setIsActive(productDetails.getIsActive());
                    existingProduct.setSku(productDetails.getSku());
                    existingProduct.setBarcode(productDetails.getBarcode());
                    existingProduct.setBrand(productDetails.getBrand());
                    existingProduct.setManufacturer(productDetails.getManufacturer());
                    existingProduct.setMinimumOrderQuantity(productDetails.getMinimumOrderQuantity());
                    existingProduct.setMaximumOrderQuantity(productDetails.getMaximumOrderQuantity());
                    existingProduct.setSubscriptionAvailable(productDetails.getSubscriptionAvailable());
                    existingProduct.setRecommendedSubscriptionPeriod(productDetails.getRecommendedSubscriptionPeriod());
                    existingProduct.setShippingFee(productDetails.getShippingFee());
                    existingProduct.setFreeShippingThreshold(productDetails.getFreeShippingThreshold());
                    existingProduct.setCountryOfOrigin(productDetails.getCountryOfOrigin());
                    existingProduct.setWeight(productDetails.getWeight());
                    existingProduct.setOriginalWeight(productDetails.getOriginalWeight());
                    existingProduct.setFeatured(productDetails.getFeatured());
                    existingProduct.setBestseller(productDetails.getBestseller());
                    existingProduct.setNewArrival(productDetails.getNewArrival());
                    
                    // 카테고리 설정 (있는 경우)
                    if (productDetails.getCategory() != null) {
                        existingProduct.setCategory(productDetails.getCategory());
                    }
                    
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional
    public void updateProductStatus(Long id, ProductStatus status) {
        productRepository.findById(id)
                .map(product -> {
                    product.setStatus(status);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional
    public void updateProductActiveStatus(Long id, Boolean isActive) {
        productRepository.findById(id)
                .map(product -> {
                    product.setIsActive(isActive);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional
    public void updateProductStock(Long id, Integer quantity) {
        productRepository.findById(id)
                .map(product -> {
                    product.setStockQuantity(quantity);
                    
                    // 재고에 따라 상태 자동 업데이트
                    if (quantity == 0) {
                        product.setStatus(ProductStatus.SOLD_OUT);
                    } else if (product.getStatus() == ProductStatus.SOLD_OUT) {
                        product.setStatus(ProductStatus.ACTIVE);
                    }
                    
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional
    public void increaseProductStock(Long id, Integer quantity) {
        productRepository.findById(id)
                .map(product -> {
                    product.addStock(quantity);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional
    public void decreaseProductStock(Long id, Integer quantity) {
        productRepository.findById(id)
                .map(product -> {
                    product.removeStock(quantity);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional
    public void applyDiscount(Long id, BigDecimal discountRate) {
        productRepository.findById(id)
                .map(product -> {
                    product.applyGmDiscount(discountRate);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional
    public void removeDiscount(Long id) {
        productRepository.findById(id)
                .map(product -> {
                    if (product.getOriginalGmPrice() != null) {
                        product.setGmPrice(product.getOriginalGmPrice());
                        product.setOriginalGmPrice(null);
                    }
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    // 상품 이미지 관련 기능
    public List<ProductImage> findProductImages(Long productId) {
        return productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
    }
    
    public Optional<ProductImage> findProductMainImage(Long productId) {
        return productImageRepository.findByProductIdAndIsMainTrue(productId);
    }
    
    @Transactional
    public void addProductImage(Long productId, ProductImage image) {
        productRepository.findById(productId)
                .map(product -> {
                    image.setProduct(product);
                    
                    // 메인 이미지로 설정된 경우, 기존 메인 이미지 해제
                    if (image.getIsMain()) {
                        productImageRepository.unsetMainImageForProduct(productId);
                    }
                    
                    return productImageRepository.save(image);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }
    
    @Transactional
    public void updateProductImage(Long imageId, String url, Boolean isMain, Integer sortOrder) {
        productImageRepository.findById(imageId)
                .map(image -> {
                    image.setUrl(url);
                    
                    // 메인 이미지로 변경된 경우
                    if (isMain && !image.getIsMain()) {
                        productImageRepository.unsetMainImageForProduct(image.getProduct().getId());
                        image.setIsMain(true);
                    } else {
                        image.setIsMain(isMain);
                    }
                    
                    image.setSortOrder(sortOrder);
                    return productImageRepository.save(image);
                })
                .orElseThrow(() -> new RuntimeException("Product image not found with id: " + imageId));
    }
    
    @Transactional
    public void deleteProductImage(Long imageId) {
        productImageRepository.deleteById(imageId);
    }
    
    @Transactional
    public void setMainImage(Long productId, Long imageId) {
        productImageRepository.unsetMainImageForProduct(productId);
        
        productImageRepository.findById(imageId)
                .map(image -> {
                    image.setIsMain(true);
                    return productImageRepository.save(image);
                })
                .orElseThrow(() -> new RuntimeException("Product image not found with id: " + imageId));
    }
    
    // 상품 콘텐츠 관련 기능
    public Optional<ProductContent> findProductContent(Long productId) {
        return productContentRepository.findByProductId(productId);
    }
    
    @Transactional
    public void updateProductContent(Long productId, String content, ContentStatus status) {
        productContentRepository.findByProductId(productId)
                .map(productContent -> {
                    productContent.setContent(content);
                    productContent.setStatus(status);
                    productContent.setVersion(productContent.getVersion() + 1);
                    return productContentRepository.save(productContent);
                })
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
                    
                    ProductContent newContent = new ProductContent();
                    newContent.setProduct(product);
                    newContent.setContent(content);
                    newContent.setStatus(status);
                    newContent.setVersion(1);
                    return productContentRepository.save(newContent);
                });
    }
   
    // 상태와 활성화 여부로 상품 조회 (페이징)
    
    // 카테고리와 상태, 활성화 여부로 상품 조회 (페이징)
    public Page<Product> findByCategoryAndStatusAndIsActive(ProductCategory category, ProductStatus status, Boolean isActive, Pageable pageable) {
        return productRepository.findByCategoryAndStatusAndIsActive(category, status, isActive, pageable);
    }
    
    // 상품 검색 (페이징)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
    
    // 특별 상품 조회 (페이징)
    public Page<Product> findFeaturedProducts(Pageable pageable) {
        return productRepository.findByFeaturedTrueAndStatusAndIsActive(ProductStatus.ACTIVE, true, pageable);
    }
    
    public Page<Product> findBestsellerProducts(Pageable pageable) {
        return productRepository.findByBestsellerTrueAndStatusAndIsActive(ProductStatus.ACTIVE, true, pageable);
    }
    
    public Page<Product> findNewArrivals(Pageable pageable) {
        return productRepository.findByNewArrivalTrueAndStatusAndIsActive(ProductStatus.ACTIVE, true, pageable);
    }
    
    public Page<Product> findDiscountedProducts(Pageable pageable) {
        return productRepository.findByOriginalGmPriceIsNotNullAndStatusAndIsActive(ProductStatus.ACTIVE, true, pageable);
    }
}