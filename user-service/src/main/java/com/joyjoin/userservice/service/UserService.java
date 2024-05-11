package com.joyjoin.userservice.service;

import com.joyjoin.userservice.exception.ResourceNotFoundException;
import com.joyjoin.userservice.model.Image;
import com.joyjoin.userservice.model.ImageUrl;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.service.client.postServiceApi.PostApiClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PostApiClient postApiClient;
    private final ImageService imageService;
    private final Environment env;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PostApiClient postApiClient, ImageService imageService, Environment env) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.postApiClient = postApiClient;
        this.imageService = imageService;
        this.env = env;
    }

//    public UserDto saveUser(User user) {
//        User optionalUser = userRepository.findUserByEmail(user.getEmail());
//        if (optionalUser != null) {
//            throw new EmailAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS.getErrorCode());
//        }
//        var now = LocalDateTime.now();
//        user.setCreatedOn(now);
//        user.setLastEdited(now);
//        User savedUser = userRepository.save(user);
//        return modelMapper.map(savedUser, UserDto.class);
//    }

    public User updateUser(User partialUser) {
        var existedUser = userRepository.findById(partialUser.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", partialUser.getId().toString()));
        modelMapper.map(partialUser, existedUser);
        existedUser.setLastEdited(LocalDateTime.now());
        return userRepository.save(existedUser);
    }


    public List<User> getAllUsers() {
//        List<PostDto> posts = postApiClient.getAllPosts();
        return userRepository.findAll();
    }

    public User getUser(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "id", uuid.toString()));
    }

    public List<User> getUserList(List<UUID> uuidList) {
        return userRepository.findAllById(uuidList);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User", "email", email);
        }
        return user;
    }

    public Image getAvatarUploadInformation(UUID uuid, LocalDateTime expireTime) {
        String key = uuid.toString() + "--" + UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(env.getProperty("s3.BUCKET_NAME"), key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        String AVATAR_BUCKET = env.getProperty("s3.BUCKET_NAME");
        return new Image(AVATAR_BUCKET, key, List.of(imageUploadUrl));
    }
}
