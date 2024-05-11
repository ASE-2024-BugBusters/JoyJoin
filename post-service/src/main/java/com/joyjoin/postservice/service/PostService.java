package com.joyjoin.postservice.service;

import com.joyjoin.postservice.exception.ResourceNotFoundException;
import com.joyjoin.postservice.controller.dto.LikePostRequest;
import com.joyjoin.postservice.controller.dto.UpdatePostRequest;
import com.joyjoin.postservice.model.Comment;
import com.joyjoin.postservice.model.Image;
import com.joyjoin.postservice.model.ImageUrl;
import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.*;
import com.joyjoin.postservice.modelDto.Event.EventDto;
import com.joyjoin.postservice.packer.PostPacker;
import com.joyjoin.postservice.repository.CommentRepository;
import com.joyjoin.postservice.repository.PostRepository;
import com.joyjoin.postservice.modelDto.User.UserDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    RestTemplate restTemplate;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;
    private final PostPacker postPacker;
    private final Environment env;


    public PostService(PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper, ImageService imageService, PostPacker postPacker, Environment env) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.postPacker = postPacker;
        this.env = env;
    }

    public Image getImgUploadInformation(LocalDateTime expireTime) {
        String key = String.valueOf(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(env.getProperty("s3.BUCKET_NAME"), key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(env.getProperty("s3.BUCKET_NAME"), key, List.of(imageUploadUrl));
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
//        List<Post> posts = postRepository.findAll().stream().filter(post -> post.getUserId().equals(userId)).toList();
        List<Post> posts = postRepository.findAll()
                .stream()
                .filter(post -> post.getUserId().equals(userId))
                .sorted(Comparator.comparing(Post:: getCreatedOn).reversed())
                .toList();
        return posts.stream().map(postPacker::packPost).collect(Collectors.toList());
    }

    @Transactional
    public PostWithUserInfoDto getPostById(String token, UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));
        PostDto postDto = postPacker.packPost(post);

        // Step1: Create a new list to store userIds (postDto.userId, postDto.taggedUserIds)
        List<UUID> userIdsList = new ArrayList<>();

        if(postDto.getTaggedUsersId() != null){
            userIdsList.addAll(postDto.getTaggedUsersId());
        }
        userIdsList.add(postDto.getUserId());

        // Store distinct values only
        String userIds = userIdsList.stream()
                .distinct() // Remove duplicates
                .map(Object::toString) // Convert UUID to String
                .collect(Collectors.joining(","));

        // Step2: Call getUsers API to retrieve User's information, including id, username, image, bios
        List<UserDto> usersInfo = getUsersInfoAPI(token, userIds);

        // Step3: Map user information to (postDto.userId, postDto.taggedUserIds, postDto.likedUsersId)
        PostWithUserInfoDto postWithUser = new PostWithUserInfoDto();
        postWithUser.setId(postDto.getId());
        postWithUser.setCaption(postDto.getCaption());
        postWithUser.setImages(postDto.getImages());
        postWithUser.setCreatedOn(postDto.getCreatedOn());

        // Step3.1: Map user information to (postDto.userId)
        UserDto _user = findUserById(usersInfo, postDto.getUserId());
        if (_user != null){
            postWithUser.setUser(_user);
        }

        // Step3.2: Map user information to (postDto.taggedUserIds)
        List<UserDto> _taggedpeople = new ArrayList<UserDto>();
        for (UUID userId : postDto.getTaggedUsersId()) {
            UserDto _userInfo = findUserById(usersInfo, userId);
            if (_userInfo != null) {
                _taggedpeople.add(_userInfo);
            }
        }
        postWithUser.setTaggedUsers(_taggedpeople);

        // Step4: Map eventId to event, to retrieve its title
        if (postDto.getTaggedEventId() != null) {
            EventDto eventInfo = getEventInfoAPI(token, postDto.getTaggedEventId().toString());
            if (eventInfo != null) {
                postWithUser.setTaggedEvent(eventInfo);
            }
        }

        return postWithUser;
//        return postPacker.packPost(post);
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


    public List<CommentWithUserInfoDto> getAllCommentsForPostId(String token, UUID postId) {
//        List<Comment> comments = commentRepository.findAll().stream().filter(comment -> comment.getPostId().equals(postId)).toList();

        List<Comment> comments = commentRepository.findAll()
                .stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .sorted(Comparator.comparing(Comment:: getCreatedOn))
                .toList();

        List<CommentWithUserInfoDto> result = new ArrayList<>();

        if (!comments.isEmpty()){
            // Step1: Extract unique userId and pass into API-call
            String userIds = comments.stream()
                    .map(comment -> comment.getUserId().toString()) // Convert UUID to String
                    .distinct() // Remove duplicates
                    .collect(Collectors.joining(","));

            // Step2: Call getUsers API to retrieve User's information, including id, username, image, bios
            List<UserDto> usersInfo = getUsersInfoAPI(token, userIds);

            // Step3: Map user information to each Comment object
            for (Comment comment : comments) {
                UserDto _userInfo = findUserById(usersInfo, comment.getUserId());
                if (_userInfo != null) {
                    CommentWithUserInfoDto commentWithUser = new CommentWithUserInfoDto(comment.getId(), _userInfo, comment.getPostId(), comment.getComment(), comment.getCreatedOn());
                    result.add(commentWithUser);
                }
            }
        }

        return result;
    }

    public List<UserDto> getAllLikesForPostId(String token, UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId.toString()));

        List<UserDto> usersInfo = new ArrayList<>();
        if(!post.getLikedUsersId().isEmpty()){
            // Step1: Create a new list to store userIds (post.likedUsersId)
            String userIds = post.getLikedUsersId().stream().map(Object::toString).collect(Collectors.joining(","));

            // Step2: Call getUsers API to retrieve User's information, including id, username, image, bios
            usersInfo = getUsersInfoAPI(token, userIds);
        }


        return usersInfo;
    }

    private List<UserDto> getUsersInfoAPI(String token, String userIds){
        String getUsersInfoUrl = "http://localhost:9191/user-service/api/user/users/" + userIds;
        System.out.println("URL: " + getUsersInfoUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<List<UserDto>> responseEntity =
                restTemplate.exchange(getUsersInfoUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<UserDto>>() {});
        List<UserDto> usersInfo = responseEntity.getBody();

        return usersInfo;
    }

    // Method to retrieve specific user's info in allUsersInfo
    public UserDto findUserById(List<UserDto> allUsersInfo, UUID userId) {
        Optional<UserDto> userOptional = allUsersInfo.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
        return userOptional.orElse(null); // or throw an exception if needed
    }

    // Method to retrieve specific event's info
    private EventDto getEventInfoAPI(String token, String eventId){
        String getEventInfoUrl = "http://localhost:9191/event-service/api/events/" + eventId;
        System.out.println("URL: " + getEventInfoUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<EventDto> responseEntity =
                restTemplate.exchange(getEventInfoUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<EventDto>() {});
        EventDto eventInfo = responseEntity.getBody();

        return eventInfo;
    }


}
