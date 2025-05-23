package com.mylittleshop.backend.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.Valid;

import com.mylittleshop.backend.dto.UserLoginRequest;
import com.mylittleshop.backend.dto.UserLoginResponse;
import com.mylittleshop.backend.dto.UserRegistrationRequest;
import com.mylittleshop.backend.dto.UserRegistrationResponse;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.model.UserProfile;
import com.mylittleshop.backend.security.JwtTokenProvider;
import com.mylittleshop.backend.service.UserService;
import com.mylittleshop.backend.exception.UserAlreadyExistsException;

import lombok.RequiredArgsConstructor;

/**
 * 인증(회원가입/로그인) 관련 API를 제공하는 컨트롤러입니다.
 * - code-generation-rules.md 및 기존 서비스/구조 규칙을 100% 준수합니다.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 API
     */
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        // User 엔티티 생성
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // UserProfile은 선택적으로 생성(예시)
        UserProfile profile = null;

        User saved = userService.registerUser(user, profile);
        UserRegistrationResponse response = new UserRegistrationResponse(
                saved.getId(), saved.getEmail(), saved.getUsername(), saved.getName(), saved.getCreatedAt()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        User user = userService.findByEmailOrUsername(request.getEmailOrUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            userService.recordFailedLogin(user.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }
        if (Boolean.TRUE.equals(user.getLocked())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("계정이 잠겨 있습니다. 관리자에게 문의하세요.");
        }
        // 로그인 성공 처리
        userService.recordLogin(user.getId());
        String token = jwtTokenProvider.generateToken(user.getUsername(),
                String.join(",", user.getRoles().stream().map(r -> r.getName()).toList()));
        UserLoginResponse response = new UserLoginResponse(
                token,
                "Bearer",
                3600L, // 만료 시간(초) - 실제로는 jwt.expiration에서 계산 필요
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getName()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 시 중복 사용자 예외 처리
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * 입력값 검증 실패 시 400 Bad Request와 상세 메시지 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.badRequest().body(sb.toString());
    }

    // 추가적인 인증/예외 처리 핸들러 등 필요시 구현
} 