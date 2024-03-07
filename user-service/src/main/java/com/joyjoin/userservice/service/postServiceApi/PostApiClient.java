package com.joyjoin.userservice.service.postServiceApi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.joyjoin.userservice.modelDto.postService.PostDto;
import java.util.List;

@FeignClient(url = "POST-SERVICE", value = "POST-SERVICE")
public interface PostApiClient {
    @GetMapping("post")
    List<PostDto> getAllPosts();
}
