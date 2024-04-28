package com.joyjoin.libs3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S3Config {
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String region;
}
