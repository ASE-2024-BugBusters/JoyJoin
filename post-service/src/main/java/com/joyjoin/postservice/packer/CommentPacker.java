package com.joyjoin.postservice.packer;

import com.joyjoin.postservice.model.Comment;
import com.joyjoin.postservice.modelDto.CommentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentPacker {
    private final ModelMapper modelMapper;

    @Autowired
    public CommentPacker(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CommentDto packComment(Comment comment) {
        CommentDto res = modelMapper.map(comment, CommentDto.class);
        return res;
    }
}
