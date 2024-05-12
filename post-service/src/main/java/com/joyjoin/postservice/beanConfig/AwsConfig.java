package com.joyjoin.postservice.beanConfig;

import com.joyjoin.libs3.S3Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("s3")
public class AwsConfig extends S3Config {

//    final URI endpoint = URI.create("https://s3.localhost.localstack.cloud:4566");
////    final URI endpoint = URI.create("http://localhost:4566");
//    final String ACCESS_KEY = "test";
//    final String SECRET_KEY = "test";
//    final StaticCredentialsProvider credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY));
//
//    final Region region = Region.US_EAST_1;
//
//    @Bean
//    public S3Presigner awsS3PreSigner() {
//        return S3Presigner.builder().endpointOverride(endpoint).credentialsProvider(credentials).region(region).build();
//    }
}
