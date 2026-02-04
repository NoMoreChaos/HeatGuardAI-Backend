package edu.aivle.heatguard_ai_back.dto.notice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCreateRequest {

    @JsonProperty("user_cd")
    @NotBlank
    private String userCd;

    @JsonProperty("notice_title")
    @NotBlank
    private String noticeTitle;

    @JsonProperty("cf_cd")
    private String cfCd;

    @JsonProperty("notice_type")
    @NotBlank
    private String noticeType;

    @JsonProperty("notice_content")
    private String noticeContent;

    @JsonProperty("notice_file_cd")
    private Integer noticeFileCd;   // 첨부파일 업로드 api 리턴값 / 첨부파일 없는 경우 null 또는 제거

    @JsonProperty("notice_fix_yn")
    @NotNull
    private Boolean noticeFixYn;
}
