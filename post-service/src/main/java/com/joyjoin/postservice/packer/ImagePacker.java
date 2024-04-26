package com.joyjoin.postservice.packer;

import com.joyjoin.postservice.model.Image;
import com.joyjoin.postservice.model.ImageRef;
import com.joyjoin.postservice.model.ImageUrl;
import com.joyjoin.postservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImagePacker {
    private final ImageService imageService;

    @Autowired
    public ImagePacker(ImageService imageService) {
        this.imageService = imageService;
    }

//    public List<Image> packImage(List<ImageRef> refs, LocalDateTime expireTime) {
//        return refs.stream().map(ref -> {
//            String url = imageService.getPreSignedUrlForDownload(ref.getBucket(), ref.getKey(), Duration.between(LocalDateTime.now(), expireTime));
//            ImageUrl imageUrl = new ImageUrl(url, expireTime);
//            return new Image(ref, List.of(imageUrl));
//        }).collect(Collectors.toList());
//    }
    public List<Image> packImage(List<ImageRef> refs, LocalDateTime expireTime) {
        if (refs == null) {
            // Return an empty list or handle the null case as appropriate
            return Collections.emptyList();
        }
        return refs.stream().map(ref -> {
            String url = imageService.getPreSignedUrlForDownload(ref.getBucket(), ref.getKey(), Duration.between(LocalDateTime.now(), expireTime));
            ImageUrl imageUrl = new ImageUrl(url, expireTime);
            return new Image(ref, List.of(imageUrl));
        }).collect(Collectors.toList());
    }

}
