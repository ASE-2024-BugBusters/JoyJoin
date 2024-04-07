package com.joyjoin.eventservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class ImageService {

    public final S3Presigner awsS3PreSigner;
    @Autowired
    public ImageService(S3Presigner awsS3PreSigner) {
        this.awsS3PreSigner = awsS3PreSigner;
    }

    public String getPreSignedUrlForUpload(String bucket, String key, Duration expireDuration) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(key).build();
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder().signatureDuration(expireDuration).putObjectRequest(putObjectRequest).build();
        PresignedPutObjectRequest presignedPutObjectRequest = awsS3PreSigner.presignPutObject(putObjectPresignRequest);
        return presignedPutObjectRequest.url().toExternalForm();
    }
    public String getPreSignedUrlForDownload(String bucket, String key, Duration expireDuration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder().signatureDuration(expireDuration).getObjectRequest(getObjectRequest).build();
        PresignedGetObjectRequest presignedGetObjectRequest = awsS3PreSigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toExternalForm();
    }
}
