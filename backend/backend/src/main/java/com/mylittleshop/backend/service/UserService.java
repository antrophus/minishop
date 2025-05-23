package com.mylittleshop.backend.service;

import com.mylittleshop.backend.model.Role;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.model.UserProfile;
import com.mylittleshop.backend.repository.RoleRepository;
import com.mylittleshop.backend.repository.UserProfileRepository;
import com.mylittleshop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mylittleshop.backend.exception.UserAlreadyExistsException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    // 기본 CRUD 작업
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    // 사용자 조회 기능
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByEmailOrUsername(emailOrUsername);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public List<User> findByActive(Boolean active) {
        return userRepository.findByActive(active);
    }
    
    public List<User> findByEmailVerified(Boolean emailVerified) {
        return userRepository.findByEmailVerified(emailVerified);
    }
    
    public List<User> findByLocked(Boolean locked) {
        return userRepository.findByLocked(locked);
    }
    
    public List<User> findByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }
    
    public List<User> findByGemmaMembership(Boolean isGemmaMember) {
        return userRepository.findByIsGemmaMember(isGemmaMember);
    }
    
    public List<User> findRecentlyJoined(LocalDateTime since) {
        return userRepository.findByCreatedAtAfter(since);
    }
    
    public List<User> findByNameContaining(String namePart) {
        return userRepository.findByNameContaining(namePart);
    }
    
    // 비즈니스 로직
    @Transactional
    public User registerUser(User user, UserProfile profile) {
        // 중복 체크
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        
        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 기본 설정
        user.setActive(true);
        user.setEmailVerified(false);
        user.setLocked(false);
        user.setFailedLoginAttempts(0);
        user.setCreatedAt(LocalDateTime.now());
        
        // 사용자 저장
        User savedUser = userRepository.save(user);
        
        // 기본 역할 할당
        assignRole(savedUser.getId(), "ROLE_USER");
        
        // 프로필 설정
        if (profile != null) {
            profile.setUser(savedUser);
            userProfileRepository.save(profile);
        }
        
        return savedUser;
    }
    
    @Transactional
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // 기본 정보 업데이트
                    existingUser.setName(userDetails.getName());
                    existingUser.setPhone(userDetails.getPhone());
                    existingUser.setAddress(userDetails.getAddress());
                    existingUser.setGender(userDetails.getGender());
                    
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Transactional
    public void updatePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // 새 비밀번호 설정
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    @Transactional
    public void activateUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setActive(true);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Transactional
    public void deactivateUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setActive(false);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Transactional
    public void verifyEmail(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setEmailVerified(true);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Transactional
    public void lockUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setLocked(true);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Transactional
    public void unlockUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setLocked(false);
                    user.setFailedLoginAttempts(0);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Transactional
    public void assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        user.addRole(role);
        userRepository.save(user);
    }
    
    @Transactional
    public void removeRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        user.removeRole(role);
        userRepository.save(user);
    }
    
    @Transactional
    public void updateGemmaMembership(Long userId, Boolean isGemmaMember) {
        userRepository.findById(userId)
                .map(user -> {
                    user.setIsGemmaMember(isGemmaMember);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    @Transactional
    public void updateUserProfile(Long userId, UserProfile profileDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        userProfileRepository.findByUser(user)
                .map(existingProfile -> {
                    existingProfile.setFullName(profileDetails.getFullName());
                    existingProfile.setPhone(profileDetails.getPhone());
                    existingProfile.setGender(profileDetails.getGender());
                    existingProfile.setBirthDate(profileDetails.getBirthDate());
                    
                    return userProfileRepository.save(existingProfile);
                })
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    newProfile.setFullName(profileDetails.getFullName());
                    newProfile.setPhone(profileDetails.getPhone());
                    newProfile.setGender(profileDetails.getGender());
                    newProfile.setBirthDate(profileDetails.getBirthDate());
                    
                    return userProfileRepository.save(newProfile);
                });
    }
    
    @Transactional
    public void recordLogin(Long userId) {
        userRepository.findById(userId)
                .map(user -> {
                    user.setLastLoginAt(LocalDateTime.now());
                    user.setFailedLoginAttempts(0);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    @Transactional
    public void recordFailedLogin(Long userId) {
        userRepository.findById(userId)
                .map(user -> {
                    Integer attempts = user.getFailedLoginAttempts() != null ? user.getFailedLoginAttempts() : 0;
                    user.setFailedLoginAttempts(attempts + 1);
                    
                    // 로그인 실패 횟수에 따른 계정 잠금 처리
                    if (user.getFailedLoginAttempts() >= 5) {
                        user.setLocked(true);
                    }
                    
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    @Transactional
    public void setAccountExpiry(Long userId, LocalDate expiryDate) {
        userRepository.findById(userId)
                .map(user -> {
                    user.setAccountExpireDate(expiryDate);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    // 사용자 프로필 관련 기능
    public Optional<UserProfile> findUserProfile(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }
}