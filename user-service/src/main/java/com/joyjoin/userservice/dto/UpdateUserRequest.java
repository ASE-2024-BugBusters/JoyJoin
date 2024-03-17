package com.joyjoin.userservice.dto;

import com.joyjoin.userservice.modelDto.UserDto;

import java.util.UUID;

public class UpdateUserRequest {
    UUID userId;
    UserDto user;
}
