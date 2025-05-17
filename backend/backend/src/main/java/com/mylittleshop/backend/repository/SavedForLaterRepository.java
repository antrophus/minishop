package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.SavedForLater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedForLaterRepository extends JpaRepository<SavedForLater, Long> {
} 