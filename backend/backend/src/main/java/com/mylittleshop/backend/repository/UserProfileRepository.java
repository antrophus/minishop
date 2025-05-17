package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
} 