package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
} 