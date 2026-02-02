package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "USER_TB")
public class UserEntity {
    @Id @Column(name = "USER_CD")
    private String userCd;

    @Column(name = "USER_ID", nullable = false, unique=true)
    private String userId;

    @Column(name = "USER_PW", nullable = false)
    private String userPw;

    @Column(name = "USER_NM", nullable = false)
    private String userNm;

    @Column(name = "USER_AUTH", nullable = false)
    private boolean userAuth;

    @Column(name = "CREATE_DATE", nullable = false)
    private LocalDateTime createDate;

}
