package edu.aivle.heatguard_ai_back.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    // 요청 형태 그대로
    @Schema(example = "홍길동", description = "사용자 이름")
    private String user_nm;

    @Schema(example = "user@user.com", description = "사용자 이메일")
    private String user_id;

    @Schema(example = "user1234", description = "사용자 비밀번호")
    private String user_pw;
}
