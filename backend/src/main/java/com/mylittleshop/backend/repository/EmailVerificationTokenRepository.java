package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.EmailVerificationToken;
import com.mylittleshop.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findByUserAndUsedIsFalse(User user);
} 