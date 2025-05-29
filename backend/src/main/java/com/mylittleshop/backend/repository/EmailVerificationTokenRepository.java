package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.EmailVerificationToken;
import com.mylittleshop.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findByUserAndUsedIsFalse(User user);
    
    @Query("SELECT e FROM EmailVerificationToken e JOIN FETCH e.user WHERE e.token = :token")
    Optional<EmailVerificationToken> findByTokenWithUser(@Param("token") String token);
} 