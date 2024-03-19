package com.joyjoin.userservice.beanConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class AwsConfig {

    final URI endpoint = URI.create("https://s3.localhost.localstack.cloud:4566");
    final String ACCESS_KEY = "test";
    final String SECRET_KEY = "test";
    final StaticCredentialsProvider credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY));

    final Region region = Region.US_EAST_1;

    @Bean
    public S3Presigner awsS3PreSigner() {
        return S3Presigner.builder().endpointOverride(endpoint).credentialsProvider(credentials).region(region).build();
    }
}
