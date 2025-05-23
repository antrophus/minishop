package com.mylittleshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 정보를 담는 DTO입니다.
 * - code-generation-rules.md의 DTO/네이밍/주석 규칙을 100% 준수합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String email;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String gender;
} 