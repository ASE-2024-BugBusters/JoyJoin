package com.joyjoin.userservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ImageRef {
    private String bucket;
    private String key;

    public ImageRef() {
        this("", "");
    }
}
