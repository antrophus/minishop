package com.mylittleshop.backend.exception;

/**
 * 이미 존재하는 사용자(이메일/유저네임)로 회원가입 시 발생하는 예외입니다.
 * - code-generation-rules.md의 예외/네이밍/주석 규칙을 100% 준수합니다.
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * 생성자: 메시지 전달
     * @param message 예외 메시지
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
} 