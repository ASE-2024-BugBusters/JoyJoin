package com.joyjoin.userservice.beanConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AwsConfig {
    @Bean
    public S3Client awsS3Client() {
        final String ACCESS_KEY = "test";
        final String SECRET_KEY = "test";
        Region region = Region.US_EAST_1;
        AwsCredentialsProvider credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY));
        return S3Client.builder()
                .region(region)
                .credentialsProvider(
                        credentials)
                .endpointOverride(URI.create("https://localhost.localstack.cloud:4566"))
                .build();
    }
}
