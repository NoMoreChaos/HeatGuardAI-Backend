package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "NOTICE_FILE_TB")
public class NoticeFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_FILE_CD")
    private Integer noticeFileCd;

    @Column(name = "NOTICE_FILE_NM", nullable = false)
    private String noticeFileNm;    //파일 original 명

    @Column(name = "NOTICE_CD")     //게시글 생성 api 순서로 인해 일시적 null 허용
    private Integer noticeCd;       //게시글 Cd

    @Column(name = "NOTICE_FILE_SAVE_NM", nullable = false)
    private String noticeFileSaveNm; //저장명

    @Column(name = "NOTICE_FILE_TYPE")
    private String noticeFileType;  //파일 확장자

    @Column(name = "NOTICE_FILE_SIZE")
    private Long noticeFileSize;    //파일 사이즈

    // 파일 경로 : 로컬 경로(테스트용) or S3 KEY 저장됨
    @Column(name = "NOTICE_FILE_SAVE_PATH", length = 500)
    private String noticeFileSavePath;

    @Column(name = "CREATE_DATE")   //파일 저장일시
    private LocalDateTime createDate;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }
}
