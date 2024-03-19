package com.joyjoin.userservice.packer;

import com.joyjoin.userservice.model.Image;
import com.joyjoin.userservice.model.ImageRef;
import com.joyjoin.userservice.model.ImageUrl;
import com.joyjoin.userservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ImagePacker {
    private final ImageService imageService;

    @Autowired
    public ImagePacker(ImageService imageService) {
        this.imageService = imageService;
    }

    public Image packImage(ImageRef ref, LocalDateTime expireTime) {
        LocalDateTime now = LocalDateTime.now();
        String url = imageService.getPreSignedUrlForDownload(ref.getBucket(), ref.getKey(), Duration.between(now, expireTime));
        return new Image(ref, List.of(new ImageUrl(url, expireTime)));
    }
}
