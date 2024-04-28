package com.joyjoin.libs3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;

public class ImageService {
    public final S3Presigner awsS3PreSigner;

    public ImageService(S3Config config) {
        final URI endpoint = URI.create(config.getEndpoint());
        final StaticCredentialsProvider credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(config.getAccessKey(), config.getSecretKey()));
        final Region region = Region.of(config.getRegion());
        this.awsS3PreSigner = S3Presigner.builder().endpointOverride(endpoint).credentialsProvider(credentials).region(region).build();
    }

    public String getPreSignedUrlForUpload(String bucket, String key) {
        return getPreSignedUrlForUpload(bucket, key, Duration.ofMinutes(10));
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
