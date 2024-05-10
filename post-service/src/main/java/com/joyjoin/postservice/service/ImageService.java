package com.joyjoin.postservice.service;

import com.joyjoin.libs3.S3Config;
import org.springframework.stereotype.Service;

@Service
public class ImageService extends com.joyjoin.libs3.ImageService {
    public ImageService(S3Config config) {
        super(config);
    }
}