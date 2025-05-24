package com.mylittleshop.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        String subject = "[MyLittleShop] 이메일 인증 안내";
        String verificationUrl = "http://localhost:5173/verify-email?token=" + token;
        String text = "아래 링크를 클릭하여 이메일 인증을 완료해 주세요.\n" + verificationUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
} 