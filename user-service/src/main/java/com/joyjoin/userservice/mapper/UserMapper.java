package com.joyjoin.userservice.mapper;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;

public class UserMapper {
    public static UserDto userToUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accountName(user.getAccountName())
                .createdOn(user.getCreatedOn())
                .isDeleted(user.isDeleted())
                .lastEdited(user.getLastEdited()).build();
        return userDto;
    }
}
