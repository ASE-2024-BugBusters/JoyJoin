package com.joyjoin.postservice.service;

import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.PostDto;
import com.joyjoin.postservice.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;

    public PostService(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    public PostDto savePost(Post post) {
        Post savedPost = postRepository.save(post);
        return mapper.map(savedPost, PostDto.class);
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream().map((post) ->
           mapper.map(post, PostDto.class)).collect(Collectors.toList());
    }
}
