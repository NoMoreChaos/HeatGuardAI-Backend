package edu.aivle.heatguard_ai_back.dto.notice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeCreateResponse {

    @JsonProperty("notice_cd")
    private Integer noticeCd;
}
