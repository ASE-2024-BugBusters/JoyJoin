package com.joyjoin.userservice.packer;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.userservice.service.ImageService;
import com.joyjoin.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserPacker {
    private final ImagePacker imagePacker;
    private final ModelMapper modelMapper;

    @Autowired
    public UserPacker(ImagePacker imagePacker, ModelMapper modelMapper) {
        this.imagePacker = imagePacker;
        this.modelMapper = modelMapper;
    }

    public UserDto packUser(User user) {
        var res = modelMapper.map(user, UserDto.class);
        res.setAvatar(imagePacker.packImage(user.getAvatar(), LocalDateTime.now().plusDays(1)));
        return res;
    }
}
