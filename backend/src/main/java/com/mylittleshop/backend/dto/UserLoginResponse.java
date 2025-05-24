package com.mylittleshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인 응답 정보를 담는 DTO입니다.
 * - code-generation-rules.md의 DTO/네이밍/주석 규칙을 100% 준수합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType;
    @Schema(description = "만료 시간(초)", example = "3600")
    private Long expiresIn;
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
    @Schema(description = "이메일", example = "user@example.com")
    private String email;
    @Schema(description = "유저네임", example = "username1")
    private String username;
    @Schema(description = "이름", example = "홍길동")
    private String name;
} 