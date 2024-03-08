package com.joyjoin.userservice.service.client.postServiceApi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.joyjoin.userservice.modelDto.userPostDto.PostDto;
import java.util.List;

@FeignClient(name = "POST-SERVICE")
public interface PostApiClient {
    @GetMapping("post")
    List<PostDto> getAllPosts();
}
