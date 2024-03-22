package com.joyjoin.eventservice.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public class Image extends ImageRef {

    private List<ImageUrl> urls;

    public Image() {
        super();
        this.urls = List.of();
    }

    public Image(@NotNull ImageRef ref, List<ImageUrl> urls) {
        this(ref.getBucket(), ref.getKey(), urls);
    }

    public Image(String bucket, String key, List<ImageUrl> urls) {
        super(bucket, key);
        this.urls = urls;
    }
}
