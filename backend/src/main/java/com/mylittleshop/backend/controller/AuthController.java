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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mylittleshop.backend.dto.UserLoginRequest;
import com.mylittleshop.backend.dto.UserLoginResponse;
import com.mylittleshop.backend.dto.UserRegistrationRequest;
import com.mylittleshop.backend.dto.UserRegistrationResponse;
import com.mylittleshop.backend.dto.PasswordResetRequest;
import com.mylittleshop.backend.dto.PasswordResetConfirmRequest;
import com.mylittleshop.backend.dto.ChangePasswordRequest;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.model.UserProfile;
import com.mylittleshop.backend.security.JwtTokenProvider;
import com.mylittleshop.backend.service.UserService;
import com.mylittleshop.backend.service.EmailService;
import com.mylittleshop.backend.exception.UserAlreadyExistsException;
import com.mylittleshop.backend.model.EmailVerificationToken;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 인증(회원가입/로그인) 관련 API를 제공하는 컨트롤러입니다.
 * - code-generation-rules.md 및 기존 서비스/구조 규칙을 100% 준수합니다.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증/회원가입", description = "회원가입 및 인증 관련 API")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * 회원가입 API
     */
    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 이름을 입력받아 신규 사용자를 등록합니다. 중복 이메일은 허용되지 않으며, 비밀번호는 암호화되어 저장됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    })
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
        // 이메일 인증 토큰 발급 및 메일 발송
        var emailToken = userService.createEmailVerificationTokenForUser(saved, 24 * 60 * 60 * 1000L); // 24시간 유효
        emailService.sendVerificationEmail(saved.getEmail(), emailToken.getToken());
        UserRegistrationResponse response = new UserRegistrationResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getUsername(),
                saved.getName(),
                saved.getCreatedAt(),
                true,
                "회원가입이 완료되었습니다. 이메일 인증을 진행해 주세요."
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인 API
     */
    @Operation(summary = "로그인", description = "이메일 또는 유저네임과 비밀번호로 로그인합니다. 성공 시 JWT 토큰을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공(JWT 토큰 반환)"),
        @ApiResponse(responseCode = "401", description = "비밀번호 불일치 또는 계정 잠김 등 인증 실패"),
        @ApiResponse(responseCode = "404", description = "사용자 정보 없음")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        User user = userService.findByEmailOrUsernameWithRoles(request.getEmailOrUsername())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
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
        long expiresIn = 0L;
        try {
            java.lang.reflect.Field field = jwtTokenProvider.getClass().getDeclaredField("validityInMilliseconds");
            field.setAccessible(true);
            expiresIn = ((long) field.get(jwtTokenProvider)) / 1000L;
        } catch (Exception e) {
            expiresIn = 3600L; // fallback
        }
        UserLoginResponse response = new UserLoginResponse(
                token,
                "Bearer",
                expiresIn,
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

    @Operation(summary = "비밀번호 재설정 요청", description = "이메일을 입력받아 비밀번호 재설정(Reset) 요청을 처리합니다. 이메일로 재설정 링크를 발송합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "비밀번호 재설정 이메일 발송 성공"),
        @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "해당 이메일의 사용자가 존재하지 않음")
    })
    @PostMapping("/password/reset-request")
    public ResponseEntity<String> passwordResetRequest(@Valid @RequestBody PasswordResetRequest request) {
        // 인증된 사용자인 경우에만 허용 (비로그인 사용자는 401)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자만 비밀번호 재설정 요청이 가능합니다.");
        }
        // 1. 사용자 조회
        User user = userService.findByEmail(request.getEmail())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이메일의 사용자가 존재하지 않습니다.");
        }
        // 2. 토큰 발급 (유효기간 30분)
        var token = userService.createPasswordResetTokenForUser(user, 30 * 60 * 1000L);
        // 3. 실제 서비스라면 이메일 발송, 여기서는 토큰 반환(또는 로그)
        // log.info("비밀번호 재설정 토큰: {}", token.getToken());
        return ResponseEntity.ok("비밀번호 재설정 토큰: " + token.getToken());
    }

    @Operation(summary = "비밀번호 재설정 확인", description = "토큰과 새 비밀번호를 입력받아 비밀번호를 재설정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공"),
        @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "토큰이 유효하지 않거나 만료됨")
    })
    @PostMapping("/password/reset-confirm")
    public ResponseEntity<String> passwordResetConfirm(@Valid @RequestBody PasswordResetConfirmRequest request) {
        // 인증된 사용자인 경우에만 허용 (비로그인 사용자는 401)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자만 비밀번호 재설정이 가능합니다.");
        }
        // 1. 토큰 검증
        var tokenOpt = userService.validatePasswordResetToken(request.getToken());
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("토큰이 유효하지 않거나 만료되었습니다.");
        }
        var token = tokenOpt.get();
        // 2. 비밀번호 변경
        userService.resetPassword(token.getUser().getId(), request.getNewPassword());
        // 3. 토큰 사용 처리
        userService.markPasswordResetTokenAsUsed(token);
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호와 새 비밀번호를 입력받아 인증된 사용자의 비밀번호를 변경합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
        @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "현재 비밀번호 불일치 또는 인증 실패")
    })
    @PostMapping("/password/change")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // SecurityContext에서 인증된 사용자 ID 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자만 비밀번호를 변경할 수 있습니다.");
        }
        String username;
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = authentication.getName();
        }
        // username(이메일 또는 유저네임)으로 User 조회
        User user = userService.findByEmailOrUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 정보를 찾을 수 없습니다.");
        }
        try {
            userService.updatePassword(user.getId(), request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 변경 실패: " + e.getMessage());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        var tokenOpt = userService.validateEmailVerificationToken(token);
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("토큰이 유효하지 않거나 만료되었습니다.");
        }
        EmailVerificationToken emailToken = tokenOpt.get();
        // 사용자 인증 처리
        var user = emailToken.getUser();
        userService.markEmailVerificationTokenAsUsed(emailToken);
        userService.verifyEmail(user.getId());
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }

    // 추가적인 인증/예외 처리 핸들러 등 필요시 구현
} 