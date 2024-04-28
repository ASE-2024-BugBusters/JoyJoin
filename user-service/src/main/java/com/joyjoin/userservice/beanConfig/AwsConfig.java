package com.joyjoin.userservice.beanConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "s3")
@Getter
@Setter
public class AwsConfig {
    private String ACCESS_KEY;
    private String SECRET_KEY;
    private String ENDPOINT;
    private String REGION;

    @Bean
    public S3Presigner awsS3PreSigner() {
        final URI endpoint = URI.create(ENDPOINT);
        final StaticCredentialsProvider credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY));
        final Region region = Region.of(REGION);
        return S3Presigner.builder().endpointOverride(endpoint).credentialsProvider(credentials).region(region).build();
    }
}
