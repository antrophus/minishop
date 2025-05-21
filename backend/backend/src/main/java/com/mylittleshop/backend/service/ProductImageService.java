package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.ProductImage;
import com.mylittleshop.backend.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    /**
     * 상품 이미지 조회
     * 
     * @param id 이미지 ID
     * @return 상품 이미지 Optional
     */
    @Transactional(readOnly = true)
    public Optional<ProductImage> getProductImageById(Long id) {
        return productImageRepository.findById(id);
    }

    /**
     * 상품별 이미지 조회
     * 
     * @param product 상품
     * @return 이미지 목록
     */
    @Transactional(readOnly = true)
    public List<ProductImage> getImagesByProduct(Product product) {
        return productImageRepository.findByProduct(product);
    }

    /**
     * 상품별 이미지 정렬 순서로 조회
     * 
     * @param product 상품
     * @return 정렬된 이미지 목록
     */
    @Transactional(readOnly = true)
    public List<ProductImage> getImagesByProductOrderBySortOrder(Product product) {
        return productImageRepository.findByProductOrderBySortOrderAsc(product);
    }

    /**
     * 상품 ID별 이미지 조회
     * 
     * @param productId 상품 ID
     * @return 이미지 목록
     */
    @Transactional(readOnly = true)
    public List<ProductImage> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    /**
     * 상품 ID별 이미지 정렬 순서로 조회
     * 
     * @param productId 상품 ID
     * @return 정렬된 이미지 목록
     */
    @Transactional(readOnly = true)
    public List<ProductImage> getImagesByProductIdOrderBySortOrder(Long productId) {
        return productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
    }

    /**
     * 상품의 메인 이미지 조회
     * 
     * @param product 상품
     * @return 메인 이미지 Optional
     */
    @Transactional(readOnly = true)
    public Optional<ProductImage> getMainImageByProduct(Product product) {
        return productImageRepository.findByProductAndIsMainTrue(product);
    }

    /**
     * 상품 ID의 메인 이미지 조회
     * 
     * @param productId 상품 ID
     * @return 메인 이미지 Optional
     */
    @Transactional(readOnly = true)
    public Optional<ProductImage> getMainImageByProductId(Long productId) {
        return productImageRepository.findByProductIdAndIsMainTrue(productId);
    }

    /**
     * 이미지 URL로 이미지 검색
     * 
     * @param urlPart URL 일부
     * @return 이미지 목록
     */
    @Transactional(readOnly = true)
    public List<ProductImage> getImagesByUrlContaining(String urlPart) {
        return productImageRepository.findByUrlContaining(urlPart);
    }

    /**
     * 상품 이미지 생성
     * 
     * @param product 상품
     * @param url 이미지 URL
     * @param isMain 메인 이미지 여부
     * @param sortOrder 정렬 순서
     * @return 생성된 상품 이미지
     */
    @Transactional
    public ProductImage createProductImage(Product product, String url, boolean isMain, int sortOrder) {
        // 메인 이미지로 설정하는 경우 기존 메인 이미지 해제
        if (isMain) {
            productImageRepository.unsetMainImageForProduct(product.getId());
        }
        
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setUrl(url);
        productImage.setIsMain(isMain);
        productImage.setSortOrder(sortOrder);
        
        return productImageRepository.save(productImage);
    }

    /**
     * 상품 이미지 간단 생성 (메인 아님, 순서 자동)
     * 
     * @param product 상품
     * @param url 이미지 URL
     * @return 생성된 상품 이미지
     */
    @Transactional
    public ProductImage createProductImage(Product product, String url) {
        long count = productImageRepository.countByProduct(product);
        return createProductImage(product, url, count == 0, (int) count);
    }

    /**
     * 상품 이미지 URL 업데이트
     * 
     * @param id 이미지 ID
     * @param url 새 URL
     * @return 업데이트된 상품 이미지
     */
    @Transactional
    public Optional<ProductImage> updateProductImageUrl(Long id, String url) {
        return productImageRepository.findById(id).map(image -> {
            image.setUrl(url);
            return productImageRepository.save(image);
        });
    }

    /**
     * 상품 이미지 메인 여부 업데이트
     * 
     * @param id 이미지 ID
     * @param isMain 메인 이미지 여부
     * @return 업데이트된 상품 이미지
     */
    @Transactional
    public Optional<ProductImage> updateProductImageMain(Long id, boolean isMain) {
        return productImageRepository.findById(id).map(image -> {
            if (isMain) {
                productImageRepository.unsetMainImageForProduct(image.getProduct().getId());
            }
            image.setIsMain(isMain);
            return productImageRepository.save(image);
        });
    }

    /**
     * 상품 이미지 정렬 순서 업데이트
     * 
     * @param id 이미지 ID
     * @param sortOrder 새 정렬 순서
     * @return 업데이트된 상품 이미지
     */
    @Transactional
    public Optional<ProductImage> updateProductImageSortOrder(Long id, int sortOrder) {
        return productImageRepository.findById(id).map(image -> {
            image.setSortOrder(sortOrder);
            return productImageRepository.save(image);
        });
    }

    /**
     * 상품 이미지 삭제
     * 
     * @param id 이미지 ID
     */
    @Transactional
    public void deleteProductImage(Long id) {
        productImageRepository.deleteById(id);
    }

    /**
     * 상품의 모든 이미지 삭제
     * 
     * @param product 상품
     */
    @Transactional
    public void deleteAllProductImages(Product product) {
        productImageRepository.deleteByProduct(product);
    }

    /**
     * 상품 ID의 모든 이미지 삭제
     * 
     * @param productId 상품 ID
     */
    @Transactional
    public void deleteAllProductImagesByProductId(Long productId) {
        productImageRepository.deleteByProductId(productId);
    }

    /**
     * 상품의 이미지 개수 조회
     * 
     * @param product 상품
     * @return 이미지 개수
     */
    @Transactional(readOnly = true)
    public long countImagesByProduct(Product product) {
        return productImageRepository.countByProduct(product);
    }

    /**
     * 상품 ID의 이미지 개수 조회
     * 
     * @param productId 상품 ID
     * @return 이미지 개수
     */
    @Transactional(readOnly = true)
    public long countImagesByProductId(Long productId) {
        return productImageRepository.countByProductId(productId);
    }

    /**
     * 상품 이미지 일괄 정렬 순서 업데이트
     * 
     * @param imageIds 이미지 ID 목록 (순서대로)
     */
    @Transactional
    public void updateProductImagesSortOrder(List<Long> imageIds) {
        for (int i = 0; i < imageIds.size(); i++) {
            final int sortOrder = i;
            productImageRepository.findById(imageIds.get(i)).ifPresent(image -> {
                image.setSortOrder(sortOrder);
                productImageRepository.save(image);
            });
        }
    }
}