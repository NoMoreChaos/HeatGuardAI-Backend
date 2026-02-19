package edu.aivle.heatguard_ai_back.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    private String user_nm;

    private String user_id;

    private String user_pw;
}
