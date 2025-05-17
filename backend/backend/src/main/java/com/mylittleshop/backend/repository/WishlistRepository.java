package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
} 