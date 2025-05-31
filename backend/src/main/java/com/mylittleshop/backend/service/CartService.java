package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.*;
import com.mylittleshop.backend.repository.CartItemRepository;
import com.mylittleshop.backend.repository.CartRepository;
import com.mylittleshop.backend.repository.ProductRepository;
import com.mylittleshop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    /**
     * 장바구니 저장
     * 
     * @param cart 장바구니
     * @return 저장된 장바구니
     */
    @Transactional
    public Cart save(Cart cart) { 
        return cartRepository.save(cart); 
    }
    
    /**
     * ID로 장바구니 조회
     * 
     * @param id 장바구니 ID
     * @return 장바구니 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Cart> findById(Long id) { 
        return cartRepository.findById(id); 
    }
    
    /**
     * 모든 장바구니 조회
     * 
     * @return 장바구니 목록
     */
    @Transactional(readOnly = true)
    public List<Cart> findAll() { 
        return cartRepository.findAll(); 
    }
    
    /**
     * 장바구니 삭제
     * 
     * @param id 장바구니 ID
     */
    @Transactional
    public void deleteById(Long id) { 
        cartRepository.deleteById(id); 
    }
    
    /**
     * 사용자별 장바구니 조회
     * 
     * @param user 사용자
     * @return 장바구니 목록
     */
    @Transactional(readOnly = true)
    public List<Cart> findByUser(User user) {
        return cartRepository.findByUser(user);
    }
    
    /**
     * 사용자 ID별 장바구니 조회
     * 
     * @param userId 사용자 ID
     * @return 장바구니 목록
     */
    @Transactional(readOnly = true)
    public List<Cart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    
    /**
     * 사용자의 현재 활성 장바구니 조회
     * 
     * @param user 사용자
     * @return 장바구니 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Cart> findActiveCartByUser(User user) {
        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE);
    }
    
    /**
     * 사용자 ID의 현재 활성 장바구니 조회
     * 
     * @param userId 사용자 ID
     * @return 장바구니 Optional
     */
    @Transactional(readOnly = true)
    public Optional<Cart> findActiveCartByUserId(Long userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE);
    }
    
    /**
     * 상태별 장바구니 조회
     * 
     * @param status 장바구니 상태
     * @return 장바구니 목록
     */
    @Transactional(readOnly = true)
    public List<Cart> findByStatus(CartStatus status) {
        return cartRepository.findByStatus(status);
    }
    
    /**
     * 장바구니 생성
     * 
     * @param user 사용자
     * @return 생성된 장바구니
     */
    @Transactional
    public Cart createCart(User user) {
        // 이미 활성 장바구니가 있는지 확인
        Optional<Cart> existingCart = findActiveCartByUser(user);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        // 새 장바구니 생성
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        
        return cartRepository.save(cart);
    }
    
    /**
     * 사용자 ID로 장바구니 생성
     * 
     * @param userId 사용자 ID
     * @return 생성된 장바구니
     */
    @Transactional
    public Cart createCartForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        
        return createCart(user);
    }
    
    /**
     * 장바구니에 상품 추가
     * 
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @param quantity 수량
     * @return 추가된 장바구니 아이템
     */
    @Transactional
    public CartItem addItemToCart(Long cartId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 ID입니다."));
        
        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new IllegalStateException("활성 상태의 장바구니만 아이템을 추가할 수 있습니다.");
        }
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품 ID입니다."));
        
        // 재고 확인
        Inventory inventory = inventoryService.findByProduct(product)
                .orElseThrow(() -> new IllegalStateException("해당 상품의 재고 정보가 없습니다."));
        
        if (inventory.getQuantity() < quantity) {
            throw new IllegalStateException("재고 부족으로 상품을 추가할 수 없습니다.");
        }
        
        // 이미 장바구니에 있는 상품인지 확인
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        CartItem cartItem;
        if (existingItem.isPresent()) {
            // 기존 아이템 수량 업데이트
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            // 재고 확인
            if (inventory.getQuantity() < newQuantity) {
                throw new IllegalStateException("재고 부족으로 상품 수량을 업데이트할 수 없습니다.");
            }
            
            cartItem.setQuantity(newQuantity);
        } else {
            // 새 아이템 추가
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setAddedAt(LocalDateTime.now());
        }
        
        // 상품 가격 설정 (멤버십 등급에 따라 다른 가격 적용 가능)
        if (cart.getUser().getIsGemmaMember()) {  // isGemmaMember() -> getIsGemmaMember()
            cartItem.setUnitPrice(product.getGmPrice());  // regularPrice -> gmPrice
            cartItem.setAppliedPriceType("GM_PRICE");
        } else {
            cartItem.setUnitPrice(product.getGmPrice());  // regularPrice -> gmPrice
            cartItem.setAppliedPriceType("REGULAR_PRICE");
        }
        
        // 장바구니 업데이트 시간 갱신
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return cartItemRepository.save(cartItem);
    }
    
    /**
     * 사용자의 장바구니에 상품 추가
     * 
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @param quantity 수량
     * @return 추가된 장바구니 아이템
     */
    @Transactional
    public CartItem addItemToUserCart(Long userId, Long productId, int quantity) {
        // 사용자의 활성 장바구니 조회
        Cart cart = findActiveCartByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));
        
        return addItemToCart(cart.getId(), productId, quantity);
    }
    
    /**
     * 장바구니 아이템 수량 업데이트
     * 
     * @param cartItemId 장바구니 아이템 ID
     * @param quantity 새 수량
     * @return 업데이트된 장바구니 아이템
     */
    @Transactional
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 아이템 ID입니다."));
        
        // 재고 확인
        Inventory inventory = inventoryService.findByProduct(cartItem.getProduct())
                .orElseThrow(() -> new IllegalStateException("해당 상품의 재고 정보가 없습니다."));
        
        if (inventory.getQuantity() < quantity) {
            throw new IllegalStateException("재고 부족으로 상품 수량을 업데이트할 수 없습니다.");
        }
        
        cartItem.setQuantity(quantity);
        
        // 장바구니 업데이트 시간 갱신
        Cart cart = cartItem.getCart();
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return cartItemRepository.save(cartItem);
    }
    
    /**
     * 장바구니에서 아이템 제거
     * 
     * @param cartItemId 장바구니 아이템 ID
     */
    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 아이템 ID입니다."));
        
        // 장바구니 업데이트 시간 갱신
        Cart cart = cartItem.getCart();
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        cartItemRepository.delete(cartItem);
    }
    
    /**
     * 장바구니 비우기
     * 
     * @param cartId 장바구니 ID
     * @return 제거된 아이템 수
     */
    @Transactional
    public int clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 ID입니다."));
        
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        int count = cartItems.size();
        
        cartItemRepository.deleteAll(cartItems);
        
        // 장바구니 업데이트 시간 갱신
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return count;
    }
    
    /**
     * 사용자의 장바구니 비우기
     * 
     * @param userId 사용자 ID
     * @return 제거된 아이템 수
     */
    @Transactional
    public int clearUserCart(Long userId) {
        Optional<Cart> cartOpt = findActiveCartByUserId(userId);
        if (!cartOpt.isPresent()) {
            return 0;
        }
        
        return clearCart(cartOpt.get().getId());
    }
    
    /**
     * 장바구니 상태 변경
     * 
     * @param cartId 장바구니 ID
     * @param status 새 상태
     * @return 업데이트된 장바구니
     */
    @Transactional
    public Cart updateCartStatus(Long cartId, CartStatus status) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 ID입니다."));
        
        cart.setStatus(status);
        cart.setUpdatedAt(LocalDateTime.now());
        
        return cartRepository.save(cart);
    }
    
    /**
     * 장바구니 메모 업데이트
     * 
     * @param cartId 장바구니 ID
     * @param notes 메모
     * @return 업데이트된 장바구니
     */
    @Transactional
    public Cart updateCartNotes(Long cartId, String notes) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 ID입니다."));
        
        cart.setNotes(notes);
        cart.setUpdatedAt(LocalDateTime.now());
        
        return cartRepository.save(cart);
    }
    
    /**
     * 장바구니 총액 계산
     * 
     * @param cartId 장바구니 ID
     * @return 총액
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 ID입니다."));
        
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        
        return cartItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 장바구니 아이템 수 조회
     * 
     * @param cartId 장바구니 ID
     * @return 아이템 수
     */
    @Transactional(readOnly = true)
    public long countCartItems(Long cartId) {
        return cartItemRepository.countByCartId(cartId);
    }
    
    /**
     * 사용자의 장바구니 아이템 수 조회
     * 
     * @param userId 사용자 ID
     * @return 아이템 수
     */
    @Transactional(readOnly = true)
    public long countUserCartItems(Long userId) {
        Optional<Cart> cartOpt = findActiveCartByUserId(userId);
        if (!cartOpt.isPresent()) {
            return 0;
        }
        
        return countCartItems(cartOpt.get().getId());
    }
    
    /**
     * 장바구니 아이템 조회
     * 
     * @param cartId 장바구니 ID
     * @return 장바구니 아이템 목록
     */
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 ID입니다."));
        
        return cartItemRepository.findByCart(cart);
    }
    
    /**
     * 사용자의 장바구니 아이템 조회
     * 
     * @param userId 사용자 ID
     * @return 장바구니 아이템 목록
     */
    @Transactional(readOnly = true)
    public List<CartItem> getUserCartItems(Long userId) {
        Optional<Cart> cartOpt = findActiveCartByUserId(userId);
        if (!cartOpt.isPresent()) {
            return List.of();
        }
        
        return getCartItems(cartOpt.get().getId());
    }
    
    /**
     * 오래된 장바구니 정리
     * 
     * @param days 일수
     * @return 정리된 장바구니 수
     */
    @Transactional
    public int cleanupOldCarts(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        List<Cart> oldCarts = cartRepository.findByStatusAndUpdatedAtBefore(CartStatus.ACTIVE, cutoffDate);
        
        for (Cart cart : oldCarts) {
            cart.setStatus(CartStatus.ABANDONED);
            cartRepository.save(cart);
        }
        
        return oldCarts.size();
    }
    
    /**
     * 상품이 장바구니에 있는지 확인
     * 
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean isProductInCart(Long cartId, Long productId) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        return cartItemOpt.isPresent();
    }
    
    /**
     * 상품이 사용자의 장바구니에 있는지 확인
     * 
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean isProductInUserCart(Long userId, Long productId) {
        Optional<Cart> cartOpt = findActiveCartByUserId(userId);
        if (!cartOpt.isPresent()) {
            return false;
        }
        
        return isProductInCart(cartOpt.get().getId(), productId);
    }
    
    /**
     * 위시리스트에서 장바구니로 상품 이동
     * 
     * @param userId 사용자 ID
     * @param wishlistItemId 위시리스트 아이템 ID
     * @param quantity 수량
     * @return 추가된 장바구니 아이템
     */
    @Transactional
    public CartItem moveFromWishlistToCart(Long userId, Long wishlistItemId, int quantity) {
        // 위시리스트 아이템 조회는 WishlistService를 통해 처리해야 함
        // 여기서는 위시리스트 아이템의 상품 ID만 필요하다고 가정
        Wishlist wishlistItem = null; // WishlistService.findById(wishlistItemId).orElseThrow(...)
        
        // 위시리스트 아이템을 장바구니에 추가
        return addItemToUserCart(userId, wishlistItem.getProduct().getId(), quantity);
    }
    
    /**
     * 편의 메서드: addItem (Controller에서 사용)
     * addItemToUserCart의 alias
     * 
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @param quantity 수량
     * @return 추가된 장바구니 아이템
     */
    @Transactional
    public CartItem addItem(Long userId, Long productId, Integer quantity) {
        return addItemToUserCart(userId, productId, quantity);
    }
    
    /**
     * 페이징으로 모든 장바구니 조회
     * 
     * @param pageable 페이징 정보
     * @return 장바구니 페이지
     */
    @Transactional(readOnly = true)
    public Page<Cart> findAll(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }
}