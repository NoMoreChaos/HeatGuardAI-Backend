package edu.aivle.heatguard_ai_back.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SigninRequest {

    @Schema(example = "admin@admin.com", description = "이메일(로그인 ID)")
    private String id;
    @Schema(example = "admin1234", description = "비밀번호")
    private String password;
}
