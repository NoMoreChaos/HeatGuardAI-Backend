package edu.aivle.heatguard_ai_back.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${JWT_SECRET" +
            "}")
    private String secret;

    private Key key() {
        // HS256은 32바이트 이상 권장
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 다음날 00:00:00 (KST) 만료
    private Date nextMidnightKst() {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDate tomorrow = LocalDate.now(zone).plusDays(1);
        Instant exp = tomorrow.atStartOfDay(zone).toInstant();
        return Date.from(exp);
    }

    // AccessToken 생성 및 00시 토큰 만료
    public String createAccessToken(String userId, boolean isAdmin) {
        Instant now = Instant.now();

        Date exp = userId.equals("superadmin@test.com")
                ? Date.from(now.plus(30, ChronoUnit.DAYS)) // 30일
                : nextMidnightKst(); // 00시 토큰만료

        return Jwts.builder()
                .setSubject(userId)
                .claim("admin", isAdmin)
                .setIssuedAt(Date.from(now))
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            parseClaims(token); // 서명 + 만료 포함 검증
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 서명 검증 포함
    public Claims parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
