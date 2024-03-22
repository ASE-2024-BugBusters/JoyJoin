package com.joyjoin.eventservice.packer;

import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.model.ImageUrl;
import com.joyjoin.eventservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventImagePacker {
    private final ImageService imageService;

    @Autowired
    public EventImagePacker(ImageService imageService) {
        this.imageService = imageService;
    }

    public Image packImage(ImageRef ref, LocalDateTime expireTime) {
        LocalDateTime now = LocalDateTime.now();
        String url = imageService.getPreSignedUrlForDownload(ref.getBucket(), ref.getKey(), Duration.between(now, expireTime));
        return new Image(ref, List.of(new ImageUrl(url, expireTime)));
    }
}
