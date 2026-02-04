package edu.aivle.heatguard_ai_back;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class Configure {
    @Value("${personal.ssh-key}")
    private String SSH_KEY;

    @Value("${personal.ssh-username}")
    private String USERNAME;

    @Value("${personal.db-id}")
    private String DB_USERNAME;

    @Value("${personal.db-pw}")
    private String DB_PASSWORD;

    @Value("${personal.ssh-passphrase}")
    private String SSH_PASSPHRASE;
    @Bean
    public DataSource dataSource() throws Exception {
        JSch jsch = new JSch();

        // 개인키 파일 등록 (개인키에 비밀번호가 있을 경우 , 뒤에 비밀번호를 문자열로 입력)
        //jsch.addIdentity(SSH_KEY, SSH_PASSPHRASE);
        jsch.addIdentity(SSH_KEY);
        // SSH 세션 생성
        Session session = jsch.getSession(USERNAME, "aice4db.duckdns.org", 22);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // 로컬 포트 3306 → 원격 DB 3306 포워딩
        int assignedPort = session.setPortForwardingL(3306, "localhost", 3306);

        // MariaDB DataSource 설정
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.mariadb.jdbc.Driver");
        ds.setUrl("jdbc:mariadb://localhost:" + assignedPort + "/aivle4th");
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PASSWORD);
        return ds;
    }
}
