package com.mylittleshop.backend.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
     * 이메일 인증 전용 API (회원가입 전 이메일 검증)
     */
    @Operation(summary = "이메일 인증 요청", description = "회원가입 전에 이메일 주소를 먼저 인증합니다.")
    @PostMapping("/email-verification")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Map<String, Object>> requestEmailVerification(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String name = request.get("name");
            
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "이메일을 입력해주세요."));
            }
            
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "이름을 입력해주세요."));
            }
            
            // 이메일 중복 확인
            if (userService.existsByEmail(email)) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "이미 가입된 이메일입니다.")
                );
            }
            
            // 임시 사용자 생성 (비밀번호 없이)
            User tempUser = new User();
            tempUser.setEmail(email.trim().toLowerCase());
            tempUser.setUsername(email.trim().toLowerCase());
            tempUser.setName(name.trim());
            tempUser.setPassword(passwordEncoder.encode("TEMP_PASSWORD_" + System.currentTimeMillis()));
            tempUser.setEmailVerified(false);
            tempUser.setActive(false); // 임시 상태로 비활성화
            tempUser.setCreatedAt(LocalDateTime.now());
            tempUser.setUpdatedAt(LocalDateTime.now());
            
            System.out.println("임시 사용자 생성 전 - name: " + tempUser.getName());
            User savedUser = userService.save(tempUser);
            System.out.println("임시 사용자 생성 후 - name: " + savedUser.getName());
            
            // 이메일 인증 토큰 생성 및 발송 (이메일 발송 실패 시 예외로 트랜잭션 롤백)
            var emailToken = userService.createEmailVerificationTokenForUser(savedUser, 24 * 60 * 60 * 1000L);
            
            // 이메일 발송 - 실패 시 즉시 예외 발생으로 트랜잭션 롤백
            emailService.sendVerificationEmail(savedUser.getEmail(), emailToken.getToken());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "인증 이메일이 발송되었습니다.",
                "email", email
            ));
            
        } catch (RuntimeException e) {
            // 트랜잭션이 롤백되도록 RuntimeException 재발생
            throw e; // 트랜잭션 롤백을 위해 예외를 다시 던짐
        } catch (Exception e) {
            // 체크된 예외를 RuntimeException으로 변환하여 트랜잭션 롤백
            throw new RuntimeException("이메일 발송 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 이메일 인증 완료 후 회원가입 완료
     */
    @Operation(summary = "회원가입 완료", description = "이메일 인증 완료 후 비밀번호를 설정하여 회원가입을 완료합니다.")
    @PostMapping("/complete-registration")
    public ResponseEntity<Map<String, Object>> completeRegistration(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String name = request.get("name"); // name 파라미터 추가
            
            if (email == null || password == null) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "이메일과 비밀번호를 입력해주세요.")
                );
            }
            
            // 사용자 조회
            User user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "사용자를 찾을 수 없습니다.")
                );
            }
            
            System.out.println("비밀번호 설정 시 조회된 사용자 - name: " + user.getName() + ", email: " + user.getEmail());
            
            // 이메일 인증 확인
            if (!Boolean.TRUE.equals(user.getEmailVerified())) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "이메일 인증이 완료되지 않았습니다.")
                );
            }
            
            // 비밀번호 설정 및 계정 활성화
            user.setPassword(passwordEncoder.encode(password));
            user.setActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            
            // name이 전달되었고 현재 name이 null이거나 비어있으면 업데이트
            if (name != null && !name.trim().isEmpty() && 
                (user.getName() == null || user.getName().trim().isEmpty())) {
                user.setName(name.trim());
                System.out.println("이름 업데이트: " + name.trim());
            }
            
            // 기본 역할 할당 (안전하게 처리)
            try {
                userService.assignRole(user.getId(), "ROLE_USER");
            } catch (Exception roleError) {
                // Role이 없어도 회원가입은 완료되도록 처리
                System.out.println("Role 할당 실패, 하지만 회원가입은 완료됨: " + roleError.getMessage());
            }
            
            User updatedUser = userService.save(user);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원가입이 완료되었습니다.",
                "user", Map.of(
                    "id", updatedUser.getId(),
                    "email", updatedUser.getEmail(),
                    "name", updatedUser.getName()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "message", "회원가입 완료 중 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 이메일로 사용자 기본 정보 조회 (회원가입 프로세스용)
     */
    @Operation(summary = "사용자 기본 정보 조회", description = "이메일로 사용자의 기본 정보를 조회합니다.")
    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestParam String email) {
        try {
            System.out.println("사용자 정보 조회 요청 - 이메일: " + email);
            User user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                System.out.println("사용자를 찾을 수 없음 - 이메일: " + email);
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "사용자를 찾을 수 없습니다.")
                );
            }
            
            System.out.println("사용자 정보 조회 성공 - 이름: '" + user.getName() + "', 이메일: '" + user.getEmail() + "'");
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "email", user.getEmail(),
                    "name", user.getName() != null ? user.getName() : "",
                    "emailVerified", user.getEmailVerified()
                )
            ));
        } catch (Exception e) {
            System.out.println("사용자 정보 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "message", "서버 오류가 발생했습니다: " + e.getMessage())
            );
        }
    }

    /**
     * 회원가입 API (기존 유지)
     */
    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 이름을 입력받아 신규 사용자를 등록합니다. 중복 이메일은 허용되지 않으며, 비밀번호는 암호화되어 저장됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    })
    @PostMapping("/register")
    @org.springframework.transaction.annotation.Transactional
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
        // 이메일 인증 토큰 발급 및 메일 발송 (이메일 발송 실패 시 트랜잭션 롤백)
        var emailToken = userService.createEmailVerificationTokenForUser(saved, 24 * 60 * 60 * 1000L); // 24시간 유효
        try {
            emailService.sendVerificationEmail(saved.getEmail(), emailToken.getToken());
        } catch (Exception emailError) {
            // 이메일 발송 실패 시 RuntimeException을 던져서 트랜잭션 롤백
            throw new RuntimeException("이메일 발송에 실패했습니다: " + emailError.getMessage(), emailError);
        }
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
        
        // 이메일 인증 확인 (선택사항: 이메일 미인증 시 로그인 차단하려면 주석 해제)
        // if (!Boolean.TRUE.equals(user.getEmailVerified())) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 인증이 필요합니다. 이메일을 확인해 주세요.");
        // }
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
     * 이메일 발송 실패 등 RuntimeException 처리
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            Map.of("success", false, "message", "처리 중 오류가 발생했습니다: " + ex.getMessage())
        );
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
        try {
            var tokenOpt = userService.validateEmailVerificationToken(token);
            if (tokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createHtmlResponse(false, "인증 실패", 
                              "인증 링크가 유효하지 않거나 만료되었습니다. 새로운 인증 이메일을 요청해 주세요."));
            }
            
            EmailVerificationToken emailToken = tokenOpt.get();
            User user = emailToken.getUser();
            
            // 이미 인증된 사용자인지 확인
            if (Boolean.TRUE.equals(user.getEmailVerified())) {
                return ResponseEntity.ok(createHtmlResponse(true, "이미 인증 완료", 
                        "이미 이메일 인증이 완료된 계정입니다. 로그인하여 서비스를 이용해 주세요."));
            }
            
            // 사용자 인증 처리
            userService.markEmailVerificationTokenAsUsed(emailToken);
            userService.verifyEmail(user.getId());
            
            // 환영 이메일 발송 (비동기로 처리 권장)
            try {
                emailService.sendWelcomeEmail(user.getEmail(), user.getName());
            } catch (Exception e) {
                System.out.println("환영 이메일 발송 실패: " + user.getEmail() + ", 오류: " + e.getMessage());
            }
            
            return ResponseEntity.ok(createHtmlResponse(true, "이메일 인증 완료!", 
                    user.getName() + "님, 이메일 인증이 성공적으로 완료되었습니다. My Little Shop에 오신 것을 환영합니다!", 
                    user.getEmail())); // 이메일 정보 전달
                    
        } catch (Exception e) {
            System.out.println("이메일 인증 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createHtmlResponse(false, "시스템 오류", 
                          "이메일 인증 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."));
        }
    }

    /**
     * 이메일 인증 재발송 API
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam("email") String email) {
        try {
            EmailVerificationToken token = userService.resendEmailVerification(email);
            emailService.sendVerificationEmail(email, token.getToken());
            return ResponseEntity.ok("인증 이메일이 재발송되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 재발송 중 오류가 발생했습니다.");
        }
    }

    /**
     * 이메일 인증 상태 확인 API
     */
    @GetMapping("/verification-status")
    public ResponseEntity<Map<String, Object>> checkVerificationStatus(@RequestParam("email") String email) {
        try {
            User user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("verified", user.getEmailVerified());
            response.put("verifiedAt", user.getEmailVerifiedAt());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * HTML 응답 생성 헬퍼 메서드
     */
    private String createHtmlResponse(boolean success, String title, String message) {
        String iconClass = success ? "✅" : "❌";
        String colorClass = success ? "#28a745" : "#dc3545";
        String buttonText = success ? "비밀번호 설정하기" : "다시 시도하기";
        String buttonUrl = success ? "http://localhost:3000/account/signup" : "http://localhost:3000/account/signup";
        
        return String.format("""
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s</title>
                <style>
                    body { font-family: 'Malgun Gothic', Arial, sans-serif; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); margin: 0; padding: 20px; min-height: 100vh; display: flex; align-items: center; justify-content: center; }
                    .container { background: white; padding: 50px 40px; border-radius: 20px; box-shadow: 0 20px 40px rgba(0,0,0,0.1); text-align: center; max-width: 500px; width: 100%%; }
                    .icon { font-size: 80px; margin-bottom: 20px; }
                    .title { color: %s; font-size: 28px; font-weight: bold; margin-bottom: 20px; }
                    .message { color: #666; font-size: 18px; line-height: 1.6; margin-bottom: 40px; }
                    .button { display: inline-block; background: linear-gradient(135deg, #007bff, #0056b3); color: white; padding: 15px 30px; text-decoration: none; border-radius: 50px; font-weight: bold; font-size: 16px; transition: transform 0.3s; }
                    .button:hover { transform: translateY(-2px); box-shadow: 0 10px 20px rgba(0,123,255,0.3); }
                    .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; color: #999; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="icon">%s</div>
                    <h1 class="title">%s</h1>
                    <p class="message">%s</p>
                    <a href="%s" class="button">%s</a>
                    <div class="footer">
                        <p>&copy; 2025 My Little Shop</p>
                    </div>
                </div>
            </body>
            </html>
            """, title, colorClass, iconClass, title, message, buttonUrl, buttonText);
    }

    /**
     * 이메일 정보를 포함한 HTML 응답 생성 헬퍼 메서드 (회원가입 완료용)
     */
    private String createHtmlResponse(boolean success, String title, String message, String email) {
        String iconClass = success ? "✅" : "❌";
        String colorClass = success ? "#28a745" : "#dc3545";
        String buttonText = success ? "비밀번호 설정하기" : "다시 시도하기";
        String buttonUrl = success ? "http://localhost:3000/account/signup?email=" + email + "&verified=true" : "http://localhost:3000/account/signup";
        
        return String.format("""
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s</title>
                <style>
                    body { font-family: 'Malgun Gothic', Arial, sans-serif; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); margin: 0; padding: 20px; min-height: 100vh; display: flex; align-items: center; justify-content: center; }
                    .container { background: white; padding: 50px 40px; border-radius: 20px; box-shadow: 0 20px 40px rgba(0,0,0,0.1); text-align: center; max-width: 500px; width: 100%%; }
                    .icon { font-size: 80px; margin-bottom: 20px; }
                    .title { color: %s; font-size: 28px; font-weight: bold; margin-bottom: 20px; }
                    .message { color: #666; font-size: 18px; line-height: 1.6; margin-bottom: 40px; }
                    .button { display: inline-block; background: linear-gradient(135deg, #007bff, #0056b3); color: white; padding: 15px 30px; text-decoration: none; border-radius: 50px; font-weight: bold; font-size: 16px; transition: transform 0.3s; }
                    .button:hover { transform: translateY(-2px); box-shadow: 0 10px 20px rgba(0,123,255,0.3); }
                    .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; color: #999; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="icon">%s</div>
                    <h1 class="title">%s</h1>
                    <p class="message">%s</p>
                    <a href="%s" class="button">%s</a>
                    <div class="footer">
                        <p>&copy; 2025 My Little Shop</p>
                    </div>
                </div>
            </body>
            </html>
            """, title, colorClass, iconClass, title, message, buttonUrl, buttonText);
    }

    /**
     * 현재 인증된 사용자의 프로필 정보 조회
     */
    @Operation(summary = "현재 사용자 프로필 조회", description = "JWT 토큰을 통해 현재 인증된 사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getCurrentUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("success", false, "message", "인증이 필요합니다.")
                );
            }

            String username;
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
            } else {
                username = authentication.getName();
            }

            User user = userService.findByEmailOrUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("success", false, "message", "사용자를 찾을 수 없습니다.")
                );
            }

            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", user.getId());
            userProfile.put("email", user.getEmail());
            userProfile.put("username", user.getUsername());
            userProfile.put("name", user.getName());
            userProfile.put("phone", user.getPhone());
            userProfile.put("address", user.getAddress());
            userProfile.put("gender", user.getGender());
            userProfile.put("emailVerified", user.getEmailVerified());
            userProfile.put("active", user.getActive());
            userProfile.put("createdAt", user.getCreatedAt());
            userProfile.put("lastLoginAt", user.getLastLoginAt());

            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", userProfile
            ));

        } catch (Exception e) {
            System.out.println("프로필 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "message", "서버 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 테스트용 사용자 생성 및 즉시 활성화 (개발 환경에서만 사용)
     */
    @PostMapping("/create-test-user")
    public ResponseEntity<Map<String, Object>> createTestUser(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String name = request.get("name");
            
            if (email == null || password == null || name == null) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "이메일, 비밀번호, 이름을 모두 입력해주세요.")
                );
            }
            
            // 이미 존재하는 사용자인지 확인
            if (userService.existsByEmail(email)) {
                User existingUser = userService.findByEmail(email).orElse(null);
                if (existingUser != null) {
                    // 기존 사용자를 활성화 및 비밀번호 설정
                    existingUser.setPassword(passwordEncoder.encode(password));
                    existingUser.setEmailVerified(true);
                    existingUser.setActive(true);
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    userService.save(existingUser);
                    
                    return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "기존 사용자가 활성화되었습니다.",
                        "user", Map.of(
                            "id", existingUser.getId(),
                            "email", existingUser.getEmail(),
                            "name", existingUser.getName()
                        )
                    ));
                }
            }
            
            // 새 사용자 생성
            User user = new User();
            user.setEmail(email.trim().toLowerCase());
            user.setUsername(email.trim().toLowerCase());
            user.setName(name.trim());
            user.setPassword(passwordEncoder.encode(password));
            user.setEmailVerified(true);
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            
            User savedUser = userService.save(user);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "테스트 사용자가 생성되었습니다.",
                "user", Map.of(
                    "id", savedUser.getId(),
                    "email", savedUser.getEmail(),
                    "name", savedUser.getName()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "message", "사용자 생성 중 오류가 발생했습니다: " + e.getMessage())
            );
        }
    }

    /**
     * 현재 인증된 사용자의 프로필 정보 업데이트
     */
    @Operation(summary = "현재 사용자 프로필 업데이트", description = "JWT 토큰을 통해 현재 인증된 사용자의 프로필 정보를 업데이트합니다.")
    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateCurrentUserProfile(@RequestBody Map<String, Object> profileData) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("success", false, "message", "인증이 필요합니다.")
                );
            }

            String username;
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
            } else {
                username = authentication.getName();
            }

            User user = userService.findByEmailOrUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("success", false, "message", "사용자를 찾을 수 없습니다.")
                );
            }

            // 프로필 정보 업데이트 (이메일은 제외)
            if (profileData.containsKey("name") && profileData.get("name") != null) {
                user.setName(profileData.get("name").toString().trim());
            }
            if (profileData.containsKey("phone")) {
                user.setPhone(profileData.get("phone") != null ? profileData.get("phone").toString().trim() : null);
            }
            if (profileData.containsKey("address")) {
                user.setAddress(profileData.get("address") != null ? profileData.get("address").toString().trim() : null);
            }
            if (profileData.containsKey("gender")) {
                user.setGender(profileData.get("gender") != null ? profileData.get("gender").toString().trim() : null);
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userService.save(user);

            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", updatedUser.getId());
            userProfile.put("email", updatedUser.getEmail());
            userProfile.put("username", updatedUser.getUsername());
            userProfile.put("name", updatedUser.getName());
            userProfile.put("phone", updatedUser.getPhone());
            userProfile.put("address", updatedUser.getAddress());
            userProfile.put("gender", updatedUser.getGender());
            userProfile.put("emailVerified", updatedUser.getEmailVerified());
            userProfile.put("active", updatedUser.getActive());
            userProfile.put("updatedAt", updatedUser.getUpdatedAt());

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "프로필이 성공적으로 업데이트되었습니다.",
                "data", userProfile
            ));

        } catch (Exception e) {
            System.out.println("프로필 업데이트 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "message", "서버 오류가 발생했습니다.")
            );
        }
    }
} 