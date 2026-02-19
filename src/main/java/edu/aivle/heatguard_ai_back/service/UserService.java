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
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다."));

        return buildAuthResponse(user);
    }


    // 이메일 중복검증
    public void checkEmailAvailable(String email) {
        if (userRepository.existsByUserId(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }

    // 회원가입
    public Map<String, Object> signUp(SignupRequest req) {
        String email = req.getUser_id();

        // 이메일 중복 체크 (검증 API가 있어도 최종 체크 필요)
        if (userRepository.existsByUserId(email)) {
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

        return buildAuthResponse(user);
    }

    private Map<String, Object> buildAuthResponse(UserEntity user) {
        boolean isAdmin = user.isUserAuth();
        String token = jwtProvider.createAccessToken(user.getUserId(), isAdmin);

        return Map.of(
                "access_token", token,
                "user_auth", isAdmin ? "admin" : "user",
                "user_cd", user.getUserCd(),
                "user_nm", user.getUserNm(),
                "user_email", user.getUserId()
        );
    }
}
