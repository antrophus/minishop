package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Wishlist;
import com.mylittleshop.backend.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    public Wishlist save(Wishlist wishlist) { return wishlistRepository.save(wishlist); }
    public Optional<Wishlist> findById(Long id) { return wishlistRepository.findById(id); }
    public List<Wishlist> findAll() { return wishlistRepository.findAll(); }
    public void deleteById(Long id) { wishlistRepository.deleteById(id); }
} 