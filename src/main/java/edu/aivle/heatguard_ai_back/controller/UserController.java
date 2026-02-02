package edu.aivle.heatguard_ai_back.controller;

import edu.aivle.heatguard_ai_back.dto.ApiResponse;
import edu.aivle.heatguard_ai_back.dto.user.request.SigninRequest;
import edu.aivle.heatguard_ai_back.dto.user.request.SignupRequest;
import edu.aivle.heatguard_ai_back.repository.UserRepository;
import edu.aivle.heatguard_ai_back.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Login", description = "로그인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    // 로그인
    @PostMapping("/signin")
    public ApiResponse<Map<String, Object>> postSignin(
            @RequestBody SigninRequest req) {
        Map<String, Object> data = userService.signIn(req);
        return ApiResponse.success(data);
    }

    // 회원가입 이메일 검증
    @PostMapping("/signup/emailCheck")
    public String postEmailCheck() {
        return "emailCheck ok";
    }

    // 회원가입
    @PostMapping("/signup")
    public ApiResponse<Map<String, Object>> postSignup(
            @RequestBody SignupRequest req) {
        Map<String, Object> data = userService.signUp(req);
        return ApiResponse.success(data);
    }
}
