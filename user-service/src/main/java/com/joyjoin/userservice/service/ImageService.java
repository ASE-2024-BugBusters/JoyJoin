package com.joyjoin.userservice.service;

import com.joyjoin.libs3.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService extends com.joyjoin.libs3.ImageService {
    @Autowired
    public ImageService(S3Config config) {
        super(config);
    }
}
