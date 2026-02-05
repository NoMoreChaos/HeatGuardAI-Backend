package edu.aivle.heatguard_ai_back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${app.s3.region}")
    private String region;

    // aws-secret.yaml
    @Value("${aws.credentials.access-key:}")
    private String accessKey;

    @Value("${aws.credentials.secret-key:}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        var builder = S3Client.builder().region(Region.of(region));

        // 로컬에서 테스트하는 경우 (키가 있음-> yaml 키 사용)
        if (!accessKey.isBlank() && !secretKey.isBlank()) {
            AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
            builder.credentialsProvider(StaticCredentialsProvider.create(creds));
        }
        // AWS 배포하는 경우(key가 없는 경우) :  DefaultCredentialsProvider 사용
        //    - EC2/ECS/Lambda에서 IAM Role로 자동 인식됨

        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        var builder = S3Presigner.builder().region(Region.of(region));

        if (!accessKey.isBlank() && !secretKey.isBlank()) {
            AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
            builder.credentialsProvider(StaticCredentialsProvider.create(creds));
        }

        return builder.build();
    }
}
