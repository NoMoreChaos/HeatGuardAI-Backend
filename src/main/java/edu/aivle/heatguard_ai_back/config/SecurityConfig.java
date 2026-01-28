package edu.aivle.heatguard_ai_back.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 개발중 CSRF 끔
                .csrf(csrf -> csrf.disable())

                // 인증 없이 허용할 경로
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**", // 모든 api호출
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/test/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // 기본 로그인 폼
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
