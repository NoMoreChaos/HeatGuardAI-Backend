package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity
public class NoticeEntity {
    @Id @Column(name = "NOTICE_CD")
    private int notice_cd;

    @Column(name = "USER_CD")
    private String user_cd;

    @Column(name = "CF_CD")
    private String cf_cd;

    @Column(name = "NOTICE_TITLE")
    private String notice_title;

    @Column(name = "NOTICE_TYPE")
    private String notice_type;

    @Column(name = "NOTICE_CONTENT")
    private String notice_content;

    @Column(name = "NOTICE_FIX_YN")
    private boolean notice_fix_yn;

    @Column(name = "CREATE_DATE")
    private String create_date;


    @Column(name = "NOTICE_FILE_CD")
    private int notice_file_cd;
}
