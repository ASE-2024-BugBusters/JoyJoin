package com.joyjoin.postservice.controller;

import com.joyjoin.eventservice.exception.ResourceNotFoundException;
import com.joyjoin.postservice.controller.dto.CreatePostCommentRequest;
import com.joyjoin.postservice.controller.dto.CreatePostRequest;
import com.joyjoin.postservice.controller.dto.LikePostRequest;
import com.joyjoin.postservice.controller.dto.UpdatePostRequest;
import com.joyjoin.postservice.model.Comment;
import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.*;
import com.joyjoin.postservice.service.PostService;
import com.joyjoin.postservice.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(allowedHeaders = "*", originPatterns = "/**")
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    @Autowired
    public PostController(PostService postService, ImageService imageService, ModelMapper modelMapper) {
        this.postService = postService;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

//    @GetMapping("/get_upload_image_url")
//    public GetImgUploadUrlResponse getImgUploadUrl() {
//        var expireTime = LocalDateTime.now().plusMinutes(30);
//        return new GetImgUploadUrlResponse(postService.getImgUploadInformation(expireTime));
//    }

    // (1) Get All Posts
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // (2) Get All Posts by UserId
    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostDto>> getAllPostsForUserId(@PathVariable UUID id) {
        List<PostDto> posts = postService.getAllPostsForUserId(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // (3) Get Specific Post by PostId
    @GetMapping("/{id}")
    public ResponseEntity<PostWithUserInfoDto> getPostById(@RequestHeader(name="Authorization") String token, @PathVariable UUID id) {
        PostWithUserInfoDto post = postService.getPostById(token, id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // (4) Create Post
    /*{
        "userId": "09f80c29-bf3f-4b4b-a23e-d179eba80001",
        "caption":"Coconut",
        "taggedUsersId":["09f80c29-bf3f-4b4b-a23e-d179eba82822"],
        "taggedEventId":"09f80c29-bf3f-4b4b-a23e-d179eba82823",
        "likedUsersId": []
    }*/
    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostRequest request) {
        Post post = modelMapper.map(request, Post.class);
        PostDto createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // (5): Update Post
    /*{
        "caption":"Adeline",
        "taggedUsersId":["09f80c29-bf3f-4b4b-a23e-d179eba82822", "09f80c29-bf3f-4b4b-a23e-d179eba82812"],
        "taggedEventId":"09f80c29-bf3f-4b4b-a23e-d179eba82823",
        "postId": "bb13c6d8-ca16-412b-a9e4-105e20a4a1d7"
    }*/
    @PatchMapping("/update")
    public ResponseEntity<PostDto> updatePost(@RequestBody UpdatePostRequest request) {
        PostDto updatedPost = postService.updatePost(request);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // (6): Delete Post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable UUID id){
        try {
            String message = postService.deletePost(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // (7): Delete All Posts
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllPost(){
        System.out.println("deleteAll...");
        String message = postService.deleteAllPosts();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // =========Below section is related to Post's Likes========

    // (8): Like/Unlike A Specific Post
    /*{
        "postId": "2090b128-86ae-4712-a502-0bf5de817fdb",
            "likeUsersId": "09f80c29-bf3f-4b4b-a23e-d179eba82823",
            "liked": 0
    }*/
    @PatchMapping("/likes/create")
    public ResponseEntity<PostDto> likeUnlikePost(@RequestBody LikePostRequest request) {
        PostDto updatedPost = postService.likeUnlikePost(request);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // (9): Retrieve all likes under a specific post
    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserDto>> getAllLikesForPostId(@RequestHeader(name="Authorization") String token, @PathVariable UUID id) {
        List<UserDto> comments = postService.getAllLikesForPostId(token, id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // =========Below section is related to Post's Comments========
    // (10): Create a comment for a specific post

    /*{
        "userId": "09f80c29-bf3f-4b4b-a23e-d179eba82802",
            "postId": "2090b128-86ae-4712-a502-0bf5de817fdb",
            "comment": "Hello!!!"
    }*/
    @PostMapping("/comments/create")
    public ResponseEntity<CommentDto> createComment(@RequestBody CreatePostCommentRequest request) {
        Comment comment = modelMapper.map(request, Comment.class);
        CommentDto createdComment = postService.createComment(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // (11): Delete a specific comment
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID id) {
        try {
            String message = postService.deleteComment(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // (12): Retrieve all comments under a specific post
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentWithUserInfoDto>> getAllCommentsForPostId(@RequestHeader(name="Authorization") String token, @PathVariable UUID id) {
        List<CommentWithUserInfoDto> comments = postService.getAllCommentsForPostId(token, id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
