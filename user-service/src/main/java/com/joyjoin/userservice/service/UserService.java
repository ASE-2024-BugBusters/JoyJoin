package com.joyjoin.userservice.service;

import com.joyjoin.userservice.beanConfig.PasswordEncoder;
import com.joyjoin.userservice.exception.EmailAlreadyExistsException;
import com.joyjoin.userservice.exception.ErrorCode;
import com.joyjoin.userservice.exception.ResourceNotFoundException;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.service.client.postServiceApi.PostApiClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PostApiClient postApiClient;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PostApiClient postApiClient, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.postApiClient = postApiClient;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto saveUser(User user) {
        User optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser != null) {
            throw new EmailAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS.getErrorCode());
        }
        user.setPassword(passwordEncoder.passwordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public List<UserDto> getAllUsers() {
//        List<PostDto> posts = postApiClient.getAllPosts();
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);

            // only to check if the API works: id does
//            userDto.setPostDto(posts);
            return userDto;
        }).collect(Collectors.toList());
    }

    public UserDto getUser(UUID uuid) {
        User user =  userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "id", uuid.toString()));
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto getUserByEmail(String email) {
        User user =  userRepository.findUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User", "email", email);
        }
        return modelMapper.map(user, UserDto.class);
    }
}
