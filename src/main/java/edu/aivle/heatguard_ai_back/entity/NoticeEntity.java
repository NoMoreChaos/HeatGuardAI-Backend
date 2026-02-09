package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @Entity
@Table(name="NOTICE_TB")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_CD", nullable = false, length = 36)
    private Integer noticeCd;

    @Column(name = "USER_CD", nullable = false, length = 36)
    private String userCd;

    @Column(name = "CF_CD", length = 36)
    private String cfCd;

    @Column(name = "NOTICE_TITLE", nullable = false, length = 255)
    private String noticeTitle;

    @Column(name = "NOTICE_TYPE", nullable = false, length = 32)
    private String noticeType;

    @Column(name = "NOTICE_CONTENT", columnDefinition = "TEXT")
    private String noticeContent;

    @Column(name = "NOTICE_FIX_YN", nullable = false)
    private Boolean noticeFixYn = false;

    @Column(name = "CREATE_DATE",nullable = false, updatable = false)
    private LocalDateTime createDate;

    @PrePersist
    public void prePersist(){
        if (this.createDate == null){
            this.createDate = LocalDateTime.now();
        }
        if (this.noticeFixYn == null){
            this.noticeFixYn = false;
        }
    }
}




