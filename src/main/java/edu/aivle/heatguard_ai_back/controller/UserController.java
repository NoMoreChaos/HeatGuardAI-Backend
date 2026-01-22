package edu.aivle.heatguard_ai_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    // 로그인
    @PostMapping("/signin")
    public String postSignin() {
        return "login ok";
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
