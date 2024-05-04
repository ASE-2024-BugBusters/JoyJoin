package com.joyjoin.postservice.service;

import com.joyjoin.postservice.exception.ResourceNotFoundException;
import com.joyjoin.postservice.controller.dto.LikePostRequest;
import com.joyjoin.postservice.controller.dto.UpdatePostRequest;
import com.joyjoin.postservice.model.Comment;
import com.joyjoin.postservice.model.Image;
import com.joyjoin.postservice.model.ImageUrl;
import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.*;
import com.joyjoin.postservice.packer.PostPacker;
import com.joyjoin.postservice.repository.CommentRepository;
import com.joyjoin.postservice.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    private final ImageService imageService;

    private final PostPacker postPacker;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper, ImageService imageService, PostPacker postPacker) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.postPacker = postPacker;
    }

    static final String EVENT_BUCKET = "post_img";
    public Image getImgUploadInformation(LocalDateTime expireTime) {
        String key = String.valueOf(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(EVENT_BUCKET, key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(EVENT_BUCKET, key, List.of(imageUploadUrl));
    }

    public PostDto createPost(Post post) {
        Post savedPost = postRepository.save(post);
        return postPacker.packPost(savedPost);
    }

    public PostDto updatePost(UpdatePostRequest updatePostRequest) {
        UUID postId = updatePostRequest.getPostId();
        var existedPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId.toString()));
        existedPost.setCaption(updatePostRequest.getCaption());
        existedPost.setTaggedUsersId(updatePostRequest.getTaggedUsersId());
        existedPost.setTaggedEventId(updatePostRequest.getTaggedEventId());
        existedPost.setLastEdited(LocalDateTime.now());
        Post savedPost = postRepository.save(existedPost);
        return postPacker.packPost(savedPost);
    }

    public PostDto likeUnlikePost(LikePostRequest likePostRequest) {
        UUID postId = likePostRequest.getPostId();
        UUID clickLikeUserId = likePostRequest.getLikeUsersId();
        Boolean liked = likePostRequest.getLiked();
        var existedPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId.toString()));
        if (existedPost.getLikedUsersId().contains(clickLikeUserId)) {
            if (!liked){
                existedPost.getLikedUsersId().remove(clickLikeUserId);
            }
        }else{
            if(liked){
                existedPost.getLikedUsersId().add(clickLikeUserId);
            }
        }
        Post savedPost = postRepository.save(existedPost);
        return postPacker.packPost(savedPost);
    }

    public String deletePost(UUID id){
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return "Post with ID " + id + " deleted successfully";
        } else {
            throw new ResourceNotFoundException("Post", "id", id.toString());
        }
    }

    public String deleteAllPosts(){
        commentRepository.deleteAll();
        postRepository.deleteAll();
        return "Deleted all posts";
    }

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll().stream().filter(post -> !post.isDeleted()).toList();
        return posts.stream().map(postPacker::packPost).collect(Collectors.toList());
    }

    public List<PostDto> getAllPostsForUserId(UUID userId){
        List<Post> posts = postRepository.findAll().stream().filter(post -> post.getUserId().equals(userId)).toList();
        return posts.stream().map(postPacker::packPost).collect(Collectors.toList());
    }

    @Transactional
    public PostDto getPostById(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));
        return postPacker.packPost(post);
    }

    public CommentDto createComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return postPacker.packComment(savedComment);
    }

    public String deleteComment(UUID id){
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return "Comment with ID " + id + " deleted successfully";
        } else {
            throw new ResourceNotFoundException("Comment", "id", id.toString());
        }
    }

    public List<CommentDto> getAllCommentsForPostId(UUID postId) {
        List<Comment> comments = commentRepository.findAll().stream().filter(comment -> comment.getPostId().equals(postId)).toList();
        return comments.stream().map(postPacker::packComment).collect(Collectors.toList());
    }


}
