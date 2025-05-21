package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.model.Wishlist;
import com.mylittleshop.backend.repository.ProductRepository;
import com.mylittleshop.backend.repository.UserRepository;
import com.mylittleshop.backend.repository.WishlistRepository;
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
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 위시리스트 저장
     * 
     * @param wishlist 위시리스트
     * @return 저장된 위시리스트
     */
    @Transactional
    public Wishlist save(Wishlist wishlist) { 
        return wishlistRepository.save(wishlist); 
    }
    
    /**
     * ID로 위시리스트 조회
     * 
     * @param id 위시리스트 ID
     * @return 위시리스트 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Wishlist> findById(Long id) { 
        return wishlistRepository.findById(id); 
    }
    
    /**
     * 모든 위시리스트 조회
     * 
     * @return 위시리스트 목록
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findAll() { 
        return wishlistRepository.findAll(); 
    }
    
    /**
     * 위시리스트 삭제
     * 
     * @param id 위시리스트 ID
     */
    @Transactional
    public void deleteById(Long id) { 
        wishlistRepository.deleteById(id); 
    }
    
    /**
     * 사용자별 위시리스트 조회
     * 
     * @param user 사용자
     * @return 위시리스트 목록
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByUser(User user) {
        return wishlistRepository.findByUser(user);
    }
    
    /**
     * 사용자 ID별 위시리스트 조회
     * 
     * @param userId 사용자 ID
     * @return 위시리스트 목록
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }
    
    /**
     * 사용자별 위시리스트 페이징 조회
     * 
     * @param user 사용자
     * @param pageable 페이징 정보
     * @return 위시리스트 페이지
     */
    @Transactional(readOnly = true)
    public Page<Wishlist> findByUser(User user, Pageable pageable) {
        return wishlistRepository.findByUser(user, pageable);
    }
    
    /**
     * 사용자 ID별 위시리스트 페이징 조회
     * 
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 위시리스트 페이지
     */
    @Transactional(readOnly = true)
    public Page<Wishlist> findByUserId(Long userId, Pageable pageable) {
        return wishlistRepository.findByUserId(userId, pageable);
    }
    
    /**
     * 상품별 위시리스트 조회
     * 
     * @param product 상품
     * @return 위시리스트 목록
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByProduct(Product product) {
        return wishlistRepository.findByProduct(product);
    }
    
    /**
     * 상품 ID별 위시리스트 조회
     * 
     * @param productId 상품 ID
     * @return 위시리스트 목록
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByProductId(Long productId) {
        return wishlistRepository.findByProductId(productId);
    }
    
    /**
     * 상품별 위시리스트 페이징 조회
     * 
     * @param product 상품
     * @param pageable 페이징 정보
     * @return 위시리스트 페이지
     */
    @Transactional(readOnly = true)
    public Page<Wishlist> findByProduct(Product product, Pageable pageable) {
        return wishlistRepository.findByProduct(product, pageable);
    }
    
    /**
     * 상품 ID별 위시리스트 페이징 조회
     * 
     * @param productId 상품 ID
     * @param pageable 페이징 정보
     * @return 위시리스트 페이지
     */
    @Transactional(readOnly = true)
    public Page<Wishlist> findByProductId(Long productId, Pageable pageable) {
        return wishlistRepository.findByProductId(productId, pageable);
    }
    
    /**
     * 사용자와 상품으로 위시리스트 조회
     * 
     * @param user 사용자
     * @param product 상품
     * @return 위시리스트 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Wishlist> findByUserAndProduct(User user, Product product) {
        return wishlistRepository.findByUserAndProduct(user, product);
    }
    
    /**
     * 사용자 ID와 상품 ID로 위시리스트 조회
     * 
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @return 위시리스트 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId) {
        return wishlistRepository.findByUserIdAndProductId(userId, productId);
    }
    
    /**
     * 상품을 위시리스트에 추가
     * 
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @return 생성된 위시리스트
     */
    @Transactional
    public Wishlist addToWishlist(Long userId, Long productId) {
        // 이미 위시리스트에 있는지 확인
        Optional<Wishlist> existingWishlist = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existingWishlist.isPresent()) {
            return existingWishlist.get();
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품 ID입니다."));
        
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setCreatedAt(LocalDateTime.now());
        
        return wishlistRepository.save(wishlist);
    }
    
    /**
     * 상품을 위시리스트에서 제거
     * 
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @return 제거 여부
     */
    @Transactional
    public boolean removeFromWishlist(Long userId, Long productId) {
        Optional<Wishlist> existingWishlist = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existingWishlist.isPresent()) {
            wishlistRepository.delete(existingWishlist.get());
            return true;
        }
        return false;
    }
    
    /**
     * 사용자의 위시리스트 비우기
     * 
     * @param userId 사용자 ID
     * @return 제거된 항목 수
     */
    @Transactional
    public int clearWishlist(Long userId) {
        List<Wishlist> userWishlist = wishlistRepository.findByUserId(userId);
        int count = userWishlist.size();
        wishlistRepository.deleteAll(userWishlist);
        return count;
    }
    
    /**
     * 상품이 위시리스트에 있는지 확인
     * 
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean isProductInWishlist(Long userId, Long productId) {
        return wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
    
    /**
     * 사용자의 위시리스트 항목 수 조회
     * 
     * @param userId 사용자 ID
     * @return 위시리스트 항목 수
     */
    @Transactional(readOnly = true)
    public long countUserWishlistItems(Long userId) {
        return wishlistRepository.countByUserId(userId);
    }
    
    /**
     * 상품별 위시리스트 추가 수 조회
     * 
     * @param productId 상품 ID
     * @return 위시리스트 추가 수
     */
    @Transactional(readOnly = true)
    public long countWishlistsByProduct(Long productId) {
        return wishlistRepository.countByProductId(productId);
    }
    
    /**
     * 인기 있는 위시리스트 상품 조회
     * 
     * @param limit 조회 수
     * @return 상품 ID와 위시리스트 수 목록
     */
    @Transactional(readOnly = true)
    public List<Object[]> findMostWishedProducts(int limit) {
        return wishlistRepository.findMostWishedProducts(limit);
    }
    
    /**
     * 특정 기간 내에 추가된 위시리스트 조회
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return wishlistRepository.findByCreatedAtBetween(startDate, endDate);
    }
    @Transactional(readOnly = true)
    public Page<Wishlist> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return wishlistRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }
    /**
     * 특정 날짜 이후 추가된 위시리스트 조회
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByCreatedAtAfter(LocalDateTime date) {
        return wishlistRepository.findByCreatedAtAfter(date);
    }
    @Transactional(readOnly = true)
    public Page<Wishlist> findByCreatedAtAfter(LocalDateTime date, Pageable pageable) {
        return wishlistRepository.findByCreatedAtAfter(date, pageable);
    }
    /**
     * 사용자 및 추가 날짜 조건 조합 조회
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date) {
        return wishlistRepository.findByUserIdAndCreatedAtAfter(userId, date);
    }
    @Transactional(readOnly = true)
    public Page<Wishlist> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date, Pageable pageable) {
        return wishlistRepository.findByUserIdAndCreatedAtAfter(userId, date, pageable);
    }
    /**
     * 특정 사용자의 최근 위시리스트 조회
     */
    @Transactional(readOnly = true)
    public List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    @Transactional(readOnly = true)
    public Page<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
}