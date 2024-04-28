package com.joyjoin.userservice.packer;

import com.joyjoin.userservice.model.Image;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.userservice.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * {@link UserAggregator} is a class that retrieves related information about a certain user,
 * and assemble them into a {@link com.joyjoin.userservice.modelDto.UserDto}.
 */
@Component
public class UserAggregator {

    private final UserService userService;
    private final ImageAggregator imageAggregator;
    private final ModelMapper modelMapper;

    private @NotNull LocalDateTime makeDefaultAvatarExpireTime() {
        return LocalDateTime.now().plusDays(1);
    }

    @Autowired
    public UserAggregator(UserService userService, ImageAggregator imageAggregator, ModelMapper modelMapper) {
        this.userService = userService;
        this.imageAggregator = imageAggregator;
        this.modelMapper = modelMapper;
    }

    public UserDto aggregate(UUID uuid) {
        var user = userService.getUser(uuid);
        return populate(user);
    }

    public UserDto populate(User user) {
        return batchedPopulate(List.of(user)).get(0);
    }

    public List<UserDto> batchedAggregate(List<UUID> uuidList) {
        var users = userService.getUserList(uuidList);
        return batchedPopulate(users);
    }

    public List<UserDto> batchedPopulate(List<User> users) {
        var imageRefList = users.stream().map(User::getAvatar).toList();
        var images = imageAggregator.batchedAggregate(imageRefList, makeDefaultAvatarExpireTime());
        var res = new ArrayList<UserDto>();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Image image = images.get(i);
            var dto = modelMapper.map(user, UserDto.class);
            dto.setAvatar(image);
            res.add(dto);
        }
        return res;
    }
}
