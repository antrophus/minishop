package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.PasswordResetToken;
import com.mylittleshop.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUserAndUsedIsFalse(User user);
} 