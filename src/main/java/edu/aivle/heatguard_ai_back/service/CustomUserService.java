package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.entity.UserEntity;
import edu.aivle.heatguard_ai_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {
    private final UserRepository userRepository;

    // Security가 로그인 시 DB에서 사용자 정보(암호/권한) 체크
    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity user = userRepository.findByUserId(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));

        // DB 관리자 : 1 / 사용자 : 0
        boolean isAdmin = user.isUser_auth();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUser_id()) // 이메일
                .password(user.getUser_pw()) // bcrypt 형식 비밀번호
                .roles(isAdmin ? "ADMIN" : "USER")
                .build();
    }
}
