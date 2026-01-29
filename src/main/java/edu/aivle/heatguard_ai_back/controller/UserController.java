package edu.aivle.heatguard_ai_back.controller;

import edu.aivle.heatguard_ai_back.dto.user.request.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final AuthenticationManager authenticationManager;

    // 로그인
    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody SigninRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getId(), req.getPassword())
        );

        return Map.of(
                "success", true
        );
    }

    // 회원가입 이메일 검증
    @PostMapping("/join/emailCheck")
    public String postEmailCheck() {
        return "emailCheck ok";
    }

    // 회원가입
    @PostMapping("/join")
    public String postJoin() {
        return "join ok";
    }
}
