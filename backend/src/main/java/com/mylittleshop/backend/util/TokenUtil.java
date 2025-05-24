package com.mylittleshop.backend.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class TokenUtil {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    /**
     * 랜덤 토큰 생성 (URL-safe, 32바이트)
     */
    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    /**
     * 토큰 만료 여부 검증
     * @param issuedAt 토큰 발급 시각
     * @param expireMillis 만료 시간(밀리초)
     * @return 만료되었으면 true, 아니면 false
     */
    public static boolean isTokenExpired(Date issuedAt, long expireMillis) {
        long now = System.currentTimeMillis();
        return (issuedAt.getTime() + expireMillis) < now;
    }
} 