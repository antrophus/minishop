package com.mylittleshop.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylittleshop.backend.dto.UserRegistrationRequest;
import com.mylittleshop.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
} 