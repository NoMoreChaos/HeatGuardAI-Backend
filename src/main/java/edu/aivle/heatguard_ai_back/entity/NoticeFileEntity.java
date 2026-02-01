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
    private String noticeFileNm;

    @Column(name = "NOTICE_CD", nullable = false)
    private Integer noticeCd;

    @Column(name = "NOTICE_FILE_SAVE_NM", nullable = false)
    private String noticeFileSaveNm;

    @Column(name = "NOTICE_FILE_TYPE")
    private String noticeFileType;

    @Column(name = "NOTICE_FILE_SIZE")
    private Long noticeFileSize;

    @Column(name = "NOTICE_FILE_SAVE_PATH")
    private String noticeFileSavePath;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @PrePersist
    public void prePersist(){
        if (this.createDate == null){
            this.createDate = LocalDateTime.now();
        }
    }
}
