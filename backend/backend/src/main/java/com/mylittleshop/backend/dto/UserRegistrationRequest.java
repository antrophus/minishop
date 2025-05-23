package com.mylittleshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 회원가입 요청 정보를 담는 DTO입니다.
 * - code-generation-rules.md의 DTO/네이밍/주석/검증 규칙을 100% 준수합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    /** 이메일(필수, 형식 검증) */
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    /** 유저네임(필수, 3~50자) */
    @NotBlank(message = "유저네임은 필수입니다.")
    @Size(min = 3, max = 50, message = "유저네임은 3~50자여야 합니다.")
    private String username;

    /** 비밀번호(필수, 8~100자) */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    /** 이름(필수) */
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    /** 전화번호(선택) */
    private String phone;

    /** 주소(선택) */
    private String address;

    /** 성별(선택) */
    private String gender;
} 