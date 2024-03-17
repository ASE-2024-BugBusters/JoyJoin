package com.joyjoin.userservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Image {
    private String bucket;
    private String key;
    private List<String> urls;

    public Image() {
        this("", "", List.of());
    }

    @JsonProperty("uri")
    public String getUri() {
        if (bucket.isBlank()) {
            return key;
        } else if (key.isBlank()) {
            return "";
        }
        return bucket + "/" + key;
    }
}
