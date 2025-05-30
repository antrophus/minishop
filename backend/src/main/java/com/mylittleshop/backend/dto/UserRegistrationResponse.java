package com.mylittleshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 회원가입 응답 정보를 담는 DTO입니다.
 * - code-generation-rules.md의 DTO/네이밍/주석 규칙을 100% 준수합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse {
    private Long id;
    private String email;
    private String username;
    private String name;
    private LocalDateTime createdAt;
    @Schema(description = "회원가입 성공 여부", example = "true")
    private boolean success;
    @Schema(description = "응답 메시지", example = "회원가입이 완료되었습니다.")
    private String message;
} 