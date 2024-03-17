package com.joyjoin.userservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ImageService {

    public final S3Client awsS3Client;

    @Autowired
    public ImageService(S3Client awsS3Client) {
        this.awsS3Client = awsS3Client;
    }

    public String getPreSignedUrlForUpload(String bucket, String key) {
        return getPreSignedUrlForUpload(bucket, key, Duration.ofMinutes(10));
    }

    public String getPreSignedUrlForUpload(String bucket, String key, Duration expireDuration) {
        try (S3Presigner preSigner = S3Presigner.create()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(key).build();
            PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder().signatureDuration(expireDuration).putObjectRequest(putObjectRequest).build();
            PresignedPutObjectRequest presignedPutObjectRequest = preSigner.presignPutObject(putObjectPresignRequest);
            return presignedPutObjectRequest.url().toExternalForm();
        }
    }
}
