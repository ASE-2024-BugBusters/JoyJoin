package com.joyjoin.postservice.modelDto;

import com.joyjoin.postservice.model.template.DefaultProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostDto extends DefaultProperties {
    private UUID id;
    private String tag;
}
