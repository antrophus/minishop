package com.mylittleshop.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.mylittleshop.backend.security.JwtAuthenticationFilter;
import com.mylittleshop.backend.security.JwtAuthenticationEntryPoint;
import com.mylittleshop.backend.security.JwtTokenProvider;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.mylittleshop.backend.security.CustomUserDetailsService;

/**
 * Spring Security의 기본 보안 설정을 담당하는 클래스입니다.
 * - 인증/인가 정책, CORS/CSRF, PasswordEncoder Bean 등을 정의합니다.
 * - code-generation-rules.md의 패키지/네이밍/주석 규칙을 100% 준수합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder Bean을 등록합니다.
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security의 HTTP 보안 설정을 구성합니다.
     * - 인증/인가 정책, CORS, CSRF, 세션 관리 등
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception 예외 발생 시
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화(필요시 활성화)
            .cors(cors -> {}) // CORS 기본 활성화(추후 상세 설정)
            .authorizeHttpRequests(auth -> auth
                // Swagger UI 및 OpenAPI 경로는 모두 허용
                .requestMatchers(
                    "/auth/register",   // 회원가입
                    "/auth/login",      // 로그인
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173"); // 개발용 프론트엔드 주소
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService) {
        return customUserDetailsService;
    }
}
