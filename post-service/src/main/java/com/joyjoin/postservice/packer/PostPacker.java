package com.joyjoin.postservice.packer;

import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.PostDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostPacker {
    private final ImagePacker imagePacker;
    private final ModelMapper modelMapper;

    @Autowired
    public PostPacker(ImagePacker imagePacker, ModelMapper modelMapper) {
        this.imagePacker = imagePacker;
        this.modelMapper = modelMapper;
    }
    public PostDto packPost(Post post) {
        PostDto res = modelMapper.map(post, PostDto.class);
//        List<String> tagNames = Collections.emptyList();
//        if (event.getTags() != null && !event.getTags().isEmpty()) {
//            tagNames = event.getTags().stream()
//                    .map(Enum::name)
//                    .collect(Collectors.toList());
//        }
//        res.setTags(tagNames);

//        res.setImages(imagePacker.packImage(post.getImages(), LocalDateTime.now().plusDays(1)));
        res.setImages(imagePacker.packImage(post.getImages(), LocalDateTime.now().plusDays(1)));
        return res;
    }

}
