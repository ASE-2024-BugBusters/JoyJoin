package com.joyjoin.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.joyjoin.userservice.modelDto.PostDto;
import java.util.List;

@FeignClient(url = "http://localhost:8082", value = "POST-SERVICE")
public interface PostsApiClient {
    @GetMapping("post")
    List<PostDto> getAllPosts();
}
