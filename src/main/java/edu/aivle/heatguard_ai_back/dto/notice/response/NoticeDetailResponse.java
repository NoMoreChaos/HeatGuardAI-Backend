package edu.aivle.heatguard_ai_back.dto.notice.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDetailResponse {
    private String userCd;
    private String userNm;

    private Integer noticeCd;
    private String noticeTitle;
    private String noticeType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;
    private String noticeContent;

    private NoticeFile noticeFile;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NoticeFile{
        private Integer noticeFileCd;
        private String noticeFileNm;
        private String noticeFileType;
        private Long noticeFileSize;
        private String noticeFileSavePath; //s3 저장경로
    }
}
