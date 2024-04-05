package com.joyjoin.postservice.controller;

import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.PostDto;
import com.joyjoin.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostDto createPost(@RequestBody Post post) {
        return postService.savePost(post);
    }

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/test")
    public String test(){
        return "Test";
    }
}
