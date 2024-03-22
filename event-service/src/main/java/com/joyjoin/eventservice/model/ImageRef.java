package com.joyjoin.eventservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
