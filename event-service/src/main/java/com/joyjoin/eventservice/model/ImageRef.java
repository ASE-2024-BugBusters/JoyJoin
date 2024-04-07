package com.joyjoin.eventservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class ImageRef {
    private String bucket;
    private String key;

//    public ImageRef() {
//        this("", "");
//    }
}
