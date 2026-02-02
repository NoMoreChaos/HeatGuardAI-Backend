package edu.aivle.heatguard_ai_back.config;


import edu.aivle.heatguard_ai_back.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // CORS 활성화
                .cors(cors -> {})

                // 로그인 상태를 '세션'으로 기억X
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // spring security 로그인 UI/BasicAuth 안 뜨게
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())

                // 임시로 다 열어둔 상태일 경우 유지
                .authorizeHttpRequests(auth -> auth
                        // 브라우저가 사전 확인(Preflight) 요청을 보낼 때, 인증 없이 통과
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // 로그인/회원가입/이메일중복검사
                        .requestMatchers(
                                "/api/users/signin",
                                "/api/users/signup",
                                "/api/users/signup/emailCheck"
                        ).permitAll()

                        // Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 기타
                        .requestMatchers("/", "/error", "/test/**").permitAll()

                        // 공지(Notice): 작성/수정/삭제는 ADMIN만
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/notice/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/notice/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/notice/**").hasRole("ADMIN")

                        // API USER/ADMIN 둘다 토큰 필요
                        .requestMatchers("/api/**").authenticated()

                        // 인증으로 보내지 말고 그냥 막아버림
                        .anyRequest().denyAll()
                )
                // JWT 필터를 만들었으면 활성화
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 화면 개발 주소 허용
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "https://*.trycloudflare.com"
                // TODO:: 화면 배포 주소
        ));

        // 메서드 허용
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // header 허용 (Authorization 포함)
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));

        // JWT 쓰면 보통 false (쿠키 기반이면 true)
        config.setAllowCredentials(false);

        // 모든 경로에 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 유효한 로그인 판단 관리자
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}