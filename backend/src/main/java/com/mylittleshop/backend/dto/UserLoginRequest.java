package com.mylittleshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인 요청 정보를 담는 DTO입니다.
 * - code-generation-rules.md의 DTO/네이밍/주석 규칙을 100% 준수합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    @Schema(description = "이메일 또는 유저네임", example = "user@example.com", required = true)
    private String emailOrUsername;
    @Schema(description = "비밀번호", example = "password123", required = true)
    private String password;
} 