package com.joyjoin.eventservice.beanConfig;
import com.joyjoin.libs3.S3Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("s3")
public class AwsConfig extends S3Config {
}
