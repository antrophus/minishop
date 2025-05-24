package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Product;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    // 사용자별 위시리스트 조회
    List<Wishlist> findByUser(User user);
    Page<Wishlist> findByUser(User user, Pageable pageable);
    
    // 사용자 ID별 위시리스트 조회
    List<Wishlist> findByUserId(Long userId);
    Page<Wishlist> findByUserId(Long userId, Pageable pageable);
    
    // 상품별 위시리스트 조회
    List<Wishlist> findByProduct(Product product);
    Page<Wishlist> findByProduct(Product product, Pageable pageable);
    
    // 상품 ID별 위시리스트 조회
    List<Wishlist> findByProductId(Long productId);
    Page<Wishlist> findByProductId(Long productId, Pageable pageable);
    
    // 사용자 및 상품 조합 조회
    Optional<Wishlist> findByUserAndProduct(User user, Product product);
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);
    
    // 사용자별 위시리스트 항목 수 조회
    long countByUserId(Long userId);
    
    // 상품별 위시리스트 항목 수 조회
    long countByProductId(Long productId);
    
    // 특정 기간 내에 추가된 위시리스트 조회
    List<Wishlist> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Wishlist> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 특정 날짜 이후 추가된 위시리스트 조회
    List<Wishlist> findByCreatedAtAfter(LocalDateTime date);
    Page<Wishlist> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);
    
    // 사용자 및 추가 날짜 조건 조합 조회
    List<Wishlist> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date);
    Page<Wishlist> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date, Pageable pageable);
    
    // 인기 있는 위시리스트 상품 조회
    @Query("SELECT w.product.id, COUNT(w) as wishCount " +
           "FROM Wishlist w " +
           "GROUP BY w.product.id " +
           "ORDER BY wishCount DESC")
    List<Object[]> findMostWishedProducts(Pageable pageable);
    
    // 인기 있는 위시리스트 상품 조회 (제한 수)
    @Query(value = "SELECT w.product_id, COUNT(w.id) as wish_count " +
                   "FROM wishlist w " +
                   "GROUP BY w.product_id " +
                   "ORDER BY wish_count DESC " +
                   "LIMIT :limit", 
            nativeQuery = true)
    List<Object[]> findMostWishedProducts(@Param("limit") int limit);
    
    // 특정 카테고리의 상품에 대한 위시리스트 조회
    @Query("SELECT w FROM Wishlist w WHERE w.product.category.id = :categoryId")
    List<Wishlist> findByProductCategoryId(@Param("categoryId") Long categoryId);
    Page<Wishlist> findByProductCategoryId(Long categoryId, Pageable pageable);
    
    // 특정 사용자의 최근 위시리스트 조회
    List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 특정 사용자의 주어진 ID 목록의 상품 위시리스트 조회
    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId AND w.product.id IN :productIds")
    List<Wishlist> findByUserIdAndProductIdIn(@Param("userId") Long userId, @Param("productIds") List<Long> productIds);
}