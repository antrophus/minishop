package com.mylittleshop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * 이메일 발송을 담당하는 서비스 클래스입니다.
 * - 회원가입 인증 이메일, 환영 이메일 등을 HTML 형식으로 발송합니다.
 * - code-generation-rules.md의 패키지/네이밍/주석 규칙을 100% 준수합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    
    @Value("${app.mail.from-email:noreply@mylittleshop.com}")
    private String fromEmail;
    
    @Value("${app.mail.verify-url:http://localhost:8080/auth/verify-email}")
    private String baseVerifyUrl;

    /**
     * 이메일 인증 메일을 발송합니다.
     * @param to 수신자 이메일
     * @param token 인증 토큰
     */
    public void sendVerificationEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("MyLittleShop - 이메일 인증");

            String verificationUrl = baseVerifyUrl + "?token=" + token;
            String htmlContent = createVerificationEmailContent(to, verificationUrl);
            
            helper.setText(htmlContent, true); // HTML 형식으로 발송

            mailSender.send(message);
            log.info("이메일 인증 메일 발송 성공: {}", to);
            
        } catch (MailException | MessagingException e) {
            log.error("이메일 발송 실패: {}", to, e);
            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage(), e);
        }
    }
    /**
     * 이메일 인증 HTML 내용을 생성합니다.
     * @param email 사용자 이메일
     * @param verificationUrl 인증 URL
     * @return HTML 내용
     */
    private String createVerificationEmailContent(String email, String verificationUrl) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>이메일 인증</title>
                <style>
                    body { font-family: 'Malgun Gothic', Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f8f9fa; }
                    .container { background-color: white; padding: 40px 30px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
                    .header { text-align: center; border-bottom: 3px solid #007bff; padding-bottom: 20px; margin-bottom: 30px; }
                    .logo { font-size: 28px; font-weight: bold; color: #007bff; margin-bottom: 10px; }
                    .subtitle { color: #6c757d; font-size: 16px; }
                    .content { margin-bottom: 30px; line-height: 1.8; }
                    .greeting { font-size: 18px; margin-bottom: 20px; }
                    .verification-button { display: inline-block; background: linear-gradient(135deg, #007bff, #0056b3); color: white; padding: 16px 32px; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 16px; margin: 25px 0; }
                    .button-container { text-align: center; margin: 30px 0; }
                    .warning { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 20px; border-radius: 4px; margin: 25px 0; }
                    .warning-title { font-weight: bold; color: #856404; margin-bottom: 10px; }
                    .warning ul { margin: 10px 0; padding-left: 20px; }
                    .warning li { margin: 8px 0; color: #856404; }
                    .footer { border-top: 1px solid #e9ecef; padding-top: 20px; font-size: 13px; color: #6c757d; text-align: center; line-height: 1.5; }
                    .link-text { word-break: break-all; color: #007bff; font-size: 14px; background-color: #f8f9fa; padding: 10px; border-radius: 4px; margin: 15px 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">🛍️ My Little Shop</div>
                        <div class="subtitle">회원가입을 완료해주세요</div>
                    </div>
                    <div class="content">
                        <div class="greeting">안녕하세요! 👋</div>
                        <p><strong>My Little Shop</strong>에 가입해 주셔서 진심으로 감사합니다!</p>
                        <p>회원가입을 완료하려면 아래 <strong>"이메일 인증하기"</strong> 버튼을 클릭해 주세요.</p>
                        <div class="button-container">
                            <a href="%s" class="verification-button">✉️ 이메일 인증하기</a>
                        </div>                        <div class="warning">
                            <div class="warning-title">🔒 보안 안내</div>
                            <ul>
                                <li>이 링크는 <strong>24시간 후</strong>에 만료됩니다.</li>
                                <li>버튼이 작동하지 않으면 아래 링크를 복사하여 브라우저에 붙여넣으세요.</li>
                                <li>본인이 가입하지 않았다면 이 이메일을 무시하셔도 됩니다.</li>
                                <li>보안을 위해 이 링크를 다른 사람과 공유하지 마세요.</li>
                            </ul>
                        </div>
                        <p><strong>📎 인증 링크:</strong></p>
                        <div class="link-text">%s</div>
                    </div>
                    <div class="footer">
                        <p>이 이메일은 자동으로 발송되었습니다. 답장하지 마세요.</p>
                        <p>문의사항이 있으시면 고객센터로 연락해 주세요.</p>
                        <p>&copy; 2025 My Little Shop. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, verificationUrl, verificationUrl);
    }
    /**
     * 이메일 인증 완료 환영 메일을 발송합니다.
     * @param to 수신자 이메일
     * @param username 사용자명
     */
    public void sendWelcomeEmail(String to, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("MyLittleShop - 회원가입 완료! 환영합니다 🎉");

            String htmlContent = createWelcomeEmailContent(username);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("환영 이메일 발송 성공: {}", to);
            
        } catch (MailException | MessagingException e) {
            log.error("환영 이메일 발송 실패: {}", to, e);
            // 환영 메일 실패는 치명적이지 않으므로 예외를 던지지 않음
        }
    }

    /**
     * 환영 이메일 HTML 내용을 생성합니다.
     * @param username 사용자명
     * @return HTML 내용
     */
    private String createWelcomeEmailContent(String username) {
        // username이 null이거나 빈 값일 경우 기본값 사용
        String displayName = (username != null && !username.trim().isEmpty()) ? username : "회원";
        
        return String.format("""
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>환영합니다!</title>
                <style>
                    body { font-family: 'Malgun Gothic', Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f8f9fa; }
                    .container { background-color: white; padding: 40px 30px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
                    .header { text-align: center; margin-bottom: 30px; }
                    .success-icon { font-size: 60px; margin-bottom: 20px; }
                    .title { color: #28a745; font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                    .content { margin-bottom: 30px; line-height: 1.8; }
                    .shop-button { display: inline-block; background: linear-gradient(135deg, #28a745, #20c997); color: white; padding: 16px 32px; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 16px; margin: 20px 0; }
                    .button-container { text-align: center; }
                    .footer { border-top: 1px solid #e9ecef; padding-top: 20px; font-size: 13px; color: #6c757d; text-align: center; }
                </style>
            </head>            <body>
                <div class="container">
                    <div class="header">
                        <div class="success-icon">🎉</div>
                        <div class="title">이메일 인증 완료!</div>
                        <p>My Little Shop에 오신 것을 환영합니다!</p>
                    </div>
                    <div class="content">
                        <p><strong>%s</strong>님, 회원가입이 성공적으로 완료되었습니다!</p>
                        <p>이제 My Little Shop의 모든 서비스를 이용하실 수 있습니다:</p>
                        <ul>
                            <li>🛒 다양한 상품 둘러보기</li>
                            <li>❤️ 위시리스트 만들기</li>
                            <li>🚚 간편한 주문 및 배송</li>
                            <li>💰 적립금 및 혜택 받기</li>
                        </ul>
                        <div class="button-container">
                            <a href="http://localhost:3000" class="shop-button">🛍️ 쇼핑 시작하기</a>
                        </div>
                    </div>
                    <div class="footer">
                        <p>궁금한 점이 있으시면 언제든 고객센터로 문의해 주세요.</p>
                        <p>&copy; 2025 My Little Shop. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, displayName);
    }
}