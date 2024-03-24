package com.joyjoin.eventservice.service;

import com.joyjoin.eventservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joyjoin.eventservice.model.Image;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    // 假设您已经有了一个S3Service来处理与S3相关的操作
    private final S3Service s3Service;
    public final S3Presigner awsS3PreSigner;
    @Autowired
    public ImageService(ImageRepository imageRepository, S3Service s3Service, S3Presigner awsS3PreSigner) {
        this.imageRepository = imageRepository;
        this.s3Service = s3Service;
        this.awsS3PreSigner = awsS3PreSigner;
    }

    public String getUploadUrl(UUID eventId) {
        // 此处生成S3上传URL的逻辑
        // 例如，使用s3Service.generateUploadUrl()方法
        String uploadUrl = s3Service.generateUploadUrl(eventId);
        return uploadUrl;
    }

//    public Image saveImageInfo(UUID eventId, String imageUrl) {
//        Image image = new Image();
//        image.setEventId(eventId);
//        image.setImageUrl(imageUrl);
//        return imageRepository.save(image);
//    }
//    public void handleImageUploaded(UUID eventId, String imageUrl) {
//        // 确认图片已上传到S3，并获取其URL
//        // 将图片信息保存到数据库
//        saveImageInfo(eventId, imageUrl);
//    }
    public List<Image> getImagesByEventId(UUID eventId) {
        return imageRepository.findByEventId(eventId);
    }
    public String getPreSignedUrlForUpload(String bucket, String key, Duration expireDuration) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(key).build();
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder().signatureDuration(expireDuration).putObjectRequest(putObjectRequest).build();
        PresignedPutObjectRequest presignedPutObjectRequest = awsS3PreSigner.presignPutObject(putObjectPresignRequest);
        return presignedPutObjectRequest.url().toExternalForm();
    }
    // 其他必要的方法，例如根据eventId查询图片等
}
