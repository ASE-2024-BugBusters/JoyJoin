package com.joyjoin.userservice.service;

import com.joyjoin.userservice.exception.EmailAlreadyExistsException;
import com.joyjoin.userservice.exception.ErrorCode;
import com.joyjoin.userservice.exception.ResourceNotFoundException;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto saveUser(User user) {
        User optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser != null) {
            throw new EmailAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS.getErrorCode());
        }
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
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
