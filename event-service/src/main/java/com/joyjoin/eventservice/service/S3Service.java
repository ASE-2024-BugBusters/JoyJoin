package com.joyjoin.eventservice.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Presigner s3Presigner;
    private final String bucketName = "img"; // Updated to match the bucket created in LocalStack

    @Autowired
    public S3Service(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    public String generateUploadUrl(UUID eventId) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(eventId.toString())
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(p -> p.signatureDuration(Duration.ofHours(1))
                .putObjectRequest(putObjectRequest));

        return presignedRequest.url().toString();
    }
}

