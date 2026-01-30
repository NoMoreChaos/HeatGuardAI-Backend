package edu.aivle.heatguard_ai_back.controller;

import edu.aivle.heatguard_ai_back.dto.user.request.SigninRequest;
import edu.aivle.heatguard_ai_back.entity.UserEntity;
import edu.aivle.heatguard_ai_back.repository.UserRepository;
import edu.aivle.heatguard_ai_back.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody SigninRequest req) {

        UserEntity user = userRepository.findByUserId(req.getId())
                .orElseThrow(() -> new RuntimeException("이메일 검증 실패하였습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(req.getPassword(), user.getUser_pw())) {
            throw new RuntimeException("비밀번호 검증 실패하였습니다.");
        }

        boolean isAdmin = user.isUser_auth();
        String auth = isAdmin ? "admin" : "user";

        String accessToken = jwtProvider.createAccessToken(req.getId(), isAdmin);

        return Map.of(
                "success", true,
                "data", Map.of(
                        "access_token", accessToken,
                        "user_auth", auth
                )
        );
    }

    // 회원가입 이메일 검증
    @PostMapping("/signup/emailCheck")
    public String postEmailCheck() {
        return "emailCheck ok";
    }

    // 회원가입
    @PostMapping("/signup")
    public String postSignup() {
        return "signup ok";
    }
}
