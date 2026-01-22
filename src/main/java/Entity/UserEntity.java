package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

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
    private String create_date;

}
