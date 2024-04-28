package com.joyjoin.userservice.beanConfig;

import com.joyjoin.libs3.S3Config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "s3")
@Getter
@Setter
public class AwsConfig extends S3Config {
}
