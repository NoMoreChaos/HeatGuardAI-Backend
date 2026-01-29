package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @Entity(name = "USER_TB")
public class UserEntity {
    @Id @Column(name = "USER_CD")
    private String user_cd;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "USER_PW")
    private String user_pw;

    @Column(name = "USER_NM")
    private String user_nm;

    @Column(name = "USER_AUTH")
    private boolean user_auth;

    @Column(name = "CREATE_DATE")
    private LocalDateTime create_date;

}
