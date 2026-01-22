package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity
public class NoticeFileEntity {
    @Id @Column(name = "NOTICE_FILE_CD")
    private int notice_file_cd;

    @Column(name = "NOTICE_FILE_NM")
    private String notice_file_nm;

    @Column(name = "NOTICE_FILE_SAVE_NM")
    private String notice_file_save_nm;

    @Column(name = "NOTICE_FILE_TYPE")
    private String notice_file_type;

    @Column(name = "NOTICE_FILE_SIZE")
    private long notice_file_size;

    @Column(name = "NOTICE_FILE_SAVE_PATH")
    private String notice_file_save_path;

    @Column(name = "CREATE_DATE")
    private String create_date;
}
