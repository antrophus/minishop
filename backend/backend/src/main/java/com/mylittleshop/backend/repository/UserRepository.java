package com.mylittleshop.backend.repository;

import com.mylittleshop.backend.model.Role;
import com.mylittleshop.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 사용자명으로 조회
    Optional<User> findByUsername(String username);
    
    // 이메일로 조회
    Optional<User> findByEmail(String email);
    
    // 이메일 또는 사용자명으로 조회
    @Query("SELECT u FROM User u WHERE u.email = :emailOrUsername OR u.username = :emailOrUsername")
    Optional<User> findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);
    
    // 사용자명 존재 여부 확인
    boolean existsByUsername(String username);
    
    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);
    
    // 활성 상태인 사용자 조회
    List<User> findByActive(Boolean active);
    Page<User> findByActive(Boolean active, Pageable pageable);
    
    // 이메일 인증된 사용자 조회
    List<User> findByEmailVerified(Boolean emailVerified);
    Page<User> findByEmailVerified(Boolean emailVerified, Pageable pageable);
    
    // 잠금 상태인 사용자 조회
    List<User> findByLocked(Boolean locked);
    Page<User> findByLocked(Boolean locked, Pageable pageable);
    
    // 회원 상태 조합 조회
    List<User> findByActiveAndEmailVerifiedAndLocked(Boolean active, Boolean emailVerified, Boolean locked);
    Page<User> findByActiveAndEmailVerifiedAndLocked(Boolean active, Boolean emailVerified, Boolean locked, Pageable pageable);
    
    // 마지막 로그인 이후 시간으로 조회
    List<User> findByLastLoginAtAfter(LocalDateTime lastLoginTime);
    List<User> findByLastLoginAtBefore(LocalDateTime lastLoginTime);
    Page<User> findByLastLoginAtBefore(LocalDateTime lastLoginTime, Pageable pageable);
    
    // 생성일 기준 조회
    List<User> findByCreatedAtAfter(LocalDateTime createdAt);
    List<User> findByCreatedAtBefore(LocalDateTime createdAt);
    Page<User> findByCreatedAtAfter(LocalDateTime createdAt, Pageable pageable);
    
    // 회원 계정 만료일 조회
    List<User> findByAccountExpireDateBefore(LocalDate expiryDate);
    Page<User> findByAccountExpireDateBefore(LocalDate expiryDate, Pageable pageable);
    
    // 제마 회원 조회
    List<User> findByIsGemmaMember(Boolean isGemmaMember);
    Page<User> findByIsGemmaMember(Boolean isGemmaMember, Pageable pageable);
    
    // 역할별 사용자 조회
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") Role role);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);
    
    // 로그인 실패 횟수 업데이트
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = :attempts WHERE u.id = :userId")
    void updateFailedLoginAttempts(@Param("userId") Long userId, @Param("attempts") Integer attempts);
    
    // 계정 잠금 상태 업데이트
    @Modifying
    @Query("UPDATE User u SET u.locked = :locked WHERE u.id = :userId")
    void updateAccountLockStatus(@Param("userId") Long userId, @Param("locked") Boolean locked);
    
    // 마지막 로그인 시간 업데이트
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime WHERE u.id = :userId")
    void updateLastLoginTime(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);
    
    // 이름에 포함된 문자열로 검색
    List<User> findByNameContaining(String namePart);
    Page<User> findByNameContaining(String namePart, Pageable pageable);
    
    // 이메일에 포함된 문자열로 검색
    List<User> findByEmailContaining(String emailPart);
    Page<User> findByEmailContaining(String emailPart, Pageable pageable);
}