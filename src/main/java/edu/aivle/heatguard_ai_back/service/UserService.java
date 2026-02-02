package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.dto.user.request.SigninRequest;
import edu.aivle.heatguard_ai_back.dto.user.request.SignupRequest;
import edu.aivle.heatguard_ai_back.entity.UserEntity;
import edu.aivle.heatguard_ai_back.repository.UserRepository;
import edu.aivle.heatguard_ai_back.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    // 로그인
    public Map<String, Object> signIn(SigninRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getId(), req.getPassword())
        );

        UserEntity user = userRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new RuntimeException("이메일이 존재하지 않습니다."));

        return buildAuthResponse(user, authentication);
    }

    // 회원가입
    public Map<String, Object> signUp(SignupRequest req){
        String email = req.getUser_id();

        // 이메일 중복 체크 (검증 API가 있어도 최종 체크 필요)
        if(userRepository.existsByUserId(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 저장
        UserEntity user = new UserEntity();
        user.setUserCd(UUID.randomUUID().toString());
        user.setUserId(email);
        user.setUserPw(passwordEncoder.encode(req.getUser_pw()));
        user.setUserNm(req.getUser_nm());
        user.setUserAuth(false); // 기본 일반 사용자
        user.setCreateDate(LocalDateTime.now());
        // 사용자 DB저장
        userRepository.save(user);

        // Security로 인증 (가입 즉시 로그인)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.getUser_pw())
        );

        return buildAuthResponse(user, authentication);
    }

    public Map<String, Object> buildAuthResponse(UserEntity user, Authentication authentication){
        boolean isAdmin = user.isUserAuth();
        String token = jwtProvider.createAccessToken(user.getUserId(), isAdmin);

        return Map.of(
                "access_token", token,
                "user_auth", isAdmin ? "admin" : "user",
                "user_nm", user.getUserNm(),
                "user_email", user.getUserId()
        );
    }
}
