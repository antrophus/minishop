package com.mylittleshop.backend.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT 인증 실패(401) 시 JSON 에러 응답을 반환하는 엔트리포인트 클래스입니다.
 * - code-generation-rules.md의 패키지/네이밍/주석 규칙을 100% 준수합니다.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.write("{\"timestamp\":\"" + System.currentTimeMillis() + "\"," +
                "\"status\":401,\"error\":\"Unauthorized\",\"message\":\"인증이 필요합니다.\"}");
        writer.flush();
    }
} 