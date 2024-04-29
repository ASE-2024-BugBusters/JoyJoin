package com.joyjoin.userservice.service;

import com.joyjoin.userservice.exception.ResourceNotFoundException;
import com.joyjoin.userservice.model.Image;
import com.joyjoin.userservice.model.ImageUrl;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.service.client.postServiceApi.PostApiClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ImageService imageService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, ImageService imageService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
    }

    public User updateUser(User partialUser) {
        var existedUser = userRepository.findById(partialUser.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", partialUser.getId().toString()));
        modelMapper.map(partialUser, existedUser);
        existedUser.setLastEdited(LocalDateTime.now());
        return userRepository.save(existedUser);
    }

    public List<User> getAllUsers() {
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
        String uploadUrl = imageService.getPreSignedUrlForUpload("avatar", key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        String AVATAR_BUCKET = "avatar";
        return new Image(AVATAR_BUCKET, key, List.of(imageUploadUrl));
    }

    public User addFollowee(UUID followerId, UUID followeeId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User", "id", followerId.toString()));
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new ResourceNotFoundException("User", "id", followerId.toString()));
        follower.getFollowee().add(followee);
        userRepository.save(follower);
        return follower;
    }

    public User removeFollowee(UUID followerId, UUID followeeId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User", "id", followerId.toString()));
        follower.getFollowee().removeIf(user -> user.getId() == followeeId);
        userRepository.save(follower);
        return follower;
    }

    public List<User> getAllFollowee(UUID followerId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User", "id", followerId.toString()));
        return follower.getFollowee().stream().toList();
    }

    public List<User> getAllFollower(UUID followeeId) {
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new ResourceNotFoundException("User", "id", followeeId.toString()));
        return followee.getFollower().stream().toList();
    }
}
