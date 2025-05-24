package com.mylittleshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 비밀번호 재설정(Reset) 확인 정보를 담는 DTO입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmRequest {
    @NotBlank(message = "토큰은 필수입니다.")
    @Schema(description = "비밀번호 재설정 토큰", example = "abcdef123456", required = true)
    private String token;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이어야 합니다.")
    @Schema(description = "새 비밀번호", example = "newPassword123", required = true)
    private String newPassword;
} 