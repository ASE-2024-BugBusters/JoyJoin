package com.joyjoin.userservice.packer;

import com.joyjoin.userservice.model.Image;
import com.joyjoin.userservice.model.ImageRef;
import com.joyjoin.userservice.model.ImageUrl;
import com.joyjoin.userservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * {@link ImageAggregator} is a class that takes image reference and assemble an entire Image DTO.
 *
 * @see Image
 * @see ImageRef
 */
@Component
public class ImageAggregator {
    private final ImageService imageService;

    @Autowired
    public ImageAggregator(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * This method assembles image reference to an entire Image DTO with default expire time
     *
     * @param ref reference to the image that is to be assembled
     * @return the DTO
     * @see #aggregate(ImageRef, LocalDateTime)
     */
    public Image aggregate(ImageRef ref) {
        return aggregate(ref, LocalDateTime.now().plusDays(1));
    }

    /**
     * This method assembles image reference to an entire Image DTO with default expire time
     *
     * @param ref        reference to the image that is to be assembled
     * @param expireTime the time the image urls in DTO expires
     * @return the DTO
     */
    public Image aggregate(ImageRef ref, LocalDateTime expireTime) {
        if (ref == null || ref.getBucket() == null || ref.getBucket().isBlank() || ref.getKey() == null || ref.getKey().isBlank()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        String url = imageService.getPreSignedUrlForDownload(ref.getBucket(), ref.getKey(), Duration.between(now, expireTime));
        return new Image(ref, List.of(new ImageUrl(url, expireTime)));
    }

    public List<Image> batchedAggregate(List<ImageRef> refs, LocalDateTime expireTime) {
        return refs.stream().map((ImageRef ref) -> this.aggregate(ref, expireTime)).toList();
    }
}
