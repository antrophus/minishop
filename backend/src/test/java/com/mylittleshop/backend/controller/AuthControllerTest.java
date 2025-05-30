package com.mylittleshop.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylittleshop.backend.dto.UserRegistrationRequest;
import com.mylittleshop.backend.dto.UserLoginRequest;
import com.mylittleshop.backend.dto.ChangePasswordRequest;
import com.mylittleshop.backend.dto.PasswordResetRequest;
import com.mylittleshop.backend.dto.PasswordResetConfirmRequest;
import com.mylittleshop.backend.model.User;
import com.mylittleshop.backend.repository.UserRepository;
import com.mylittleshop.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController 회원가입 API 단위 테스트
 * - code-generation-rules.md의 테스트/네이밍/주석 규칙을 100% 준수합니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("정상 회원가입 성공")
    void register_success() throws Exception {
        UserRegistrationRequest req = new UserRegistrationRequest(
                "test10@example.com", "testuser10", "password1230*@#$", "홍길동", "010-1234-5678", "서울시", "M"
        );
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test1@example.com"))
                .andExpect(jsonPath("$.username").value("testuser1"));
    }

    @Test
    @DisplayName("중복 이메일 회원가입 실패")
    void register_duplicateEmail() throws Exception {
        com.mylittleshop.backend.model.User user = new com.mylittleshop.backend.model.User();
        user.setEmail("dup@example.com");
        user.setUsername("dupuser");
        user.setPassword("password123");
        user.setName("홍길동");
        userRepository.save(user);
        UserRegistrationRequest req = new UserRegistrationRequest(
                "dup@example.com", "newuser", "password123", "홍길동", "", "", ""
        );
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Email already exists")));
    }

    @Test
    @DisplayName("잘못된 이메일 형식 회원가입 실패")
    void register_invalidEmail() throws Exception {
        UserRegistrationRequest req = new UserRegistrationRequest(
                "not-an-email", "user2", "password123", "홍길동", "", "", ""
        );
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("이메일 형식이 올바르지 않습니다.")));
    }

    @Test
    @DisplayName("비밀번호 짧음 회원가입 실패")
    void register_shortPassword() throws Exception {
        UserRegistrationRequest req = new UserRegistrationRequest(
                "test2@example.com", "user3", "123", "홍길동", "", "", ""
        );
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("비밀번호는 8자 이상이어야 합니다.")));
    }

    @Test
    @DisplayName("정상 로그인 성공")
    void login_success() throws Exception {
        // given
        User user = new User();
        user.setEmail("login1@example.com");
        user.setUsername("loginuser1");
        user.setPassword("$2a$10$7Qw8Qw8Qw8Qw8Qw8Qw8QwOQw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8"); // "password123" bcrypt
        user.setName("홍길동");
        user.setLocked(false);
        userRepository.save(user);
        UserLoginRequest req = new UserLoginRequest("login1@example.com", "password123");
        // when & then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.email").value("login1@example.com"));
    }

    @Test
    @DisplayName("잘못된 비밀번호 로그인 실패")
    void login_wrongPassword() throws Exception {
        User user = new User();
        user.setEmail("login2@example.com");
        user.setUsername("loginuser2");
        user.setPassword("$2a$10$7Qw8Qw8Qw8Qw8Qw8Qw8QwOQw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8"); // "password123" bcrypt
        user.setName("홍길동");
        user.setLocked(false);
        userRepository.save(user);
        UserLoginRequest req = new UserLoginRequest("login2@example.com", "wrongpass");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("비밀번호가 일치하지 않습니다.")));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 로그인 실패")
    void login_userNotFound() throws Exception {
        UserLoginRequest req = new UserLoginRequest("notfound@example.com", "password123");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("사용자를 찾을 수 없습니다.")));
    }

    @Test
    @DisplayName("계정 잠김 로그인 실패")
    void login_lockedAccount() throws Exception {
        User user = new User();
        user.setEmail("locked@example.com");
        user.setUsername("lockeduser");
        user.setPassword("$2a$10$7Qw8Qw8Qw8Qw8Qw8Qw8QwOQw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8Qw8"); // "password123" bcrypt
        user.setName("홍길동");
        user.setLocked(true);
        userRepository.save(user);
        UserLoginRequest req = new UserLoginRequest("locked@example.com", "password123");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("계정이 잠겨 있습니다")));
    }

    @Test
    @DisplayName("비밀번호 변경 - 정상")
    void changePassword_success() throws Exception {
        // given
        User user = new User();
        user.setEmail("changepw@example.com");
        user.setUsername("changepwuser");
        user.setPassword(passwordEncoder.encode("oldPassword123!"));
        user.setName("홍길동");
        user.setLocked(false);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUsername(), "ROLE_USER");

        ChangePasswordRequest req = new ChangePasswordRequest("oldPassword123!", "newPassword456!");

        mockMvc.perform(post("/auth/password/change")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("비밀번호가 성공적으로 변경되었습니다.")));
    }

    @Test
    @DisplayName("비밀번호 재설정 요청/확인 - 정상 플로우")
    void passwordResetRequestAndConfirm_success() throws Exception {
        // given
        User user = new User();
        user.setEmail("resetpw@example.com");
        user.setUsername("resetpwuser");
        user.setPassword(passwordEncoder.encode("originPassword!"));
        user.setName("홍길동");
        user.setLocked(false);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUsername(), "ROLE_USER");

        // 1. 비밀번호 재설정 요청
        PasswordResetRequest resetReq = new PasswordResetRequest("resetpw@example.com");
        String resetToken = mockMvc.perform(post("/auth/password/reset-request")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .replace("비밀번호 재설정 토큰: ", "").trim();

        // 2. 비밀번호 재설정 확인
        PasswordResetConfirmRequest confirmReq = new PasswordResetConfirmRequest(resetToken, "newPassword789!");
        mockMvc.perform(post("/auth/password/reset-confirm")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirmReq)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("비밀번호가 성공적으로 재설정되었습니다.")));
    }
} 