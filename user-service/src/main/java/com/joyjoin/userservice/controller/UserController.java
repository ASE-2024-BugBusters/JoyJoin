package com.joyjoin.userservice.controller;


import com.joyjoin.userservice.controller.dto.GetAvatarUploadUrlResponse;
import com.joyjoin.userservice.controller.dto.UpdateUserRequest;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.userservice.packer.UserAggregator;
import com.joyjoin.userservice.service.ImageService;
import com.joyjoin.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@CrossOrigin(allowedHeaders = "*", originPatterns = "/**")
public class UserController {

    private final UserService userService;

    private final UserAggregator userAggregator;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, UserAggregator userAggregator, ModelMapper modelMapper) {
        this.userService = userService;
        this.userAggregator = userAggregator;
        this.modelMapper = modelMapper;
    }

    /**
     * this can only be user for testing, in production needs to be deleted
     *
     * @return all the users
     */
    @GetMapping()
    public List<UserDto> getAllUsers() {
        var users = userService.getAllUsers();
        return userAggregator.batchedPopulate(users);
    }

    /**
     * updateUser updates a specified user. All {@code null} fields will remain as is.
     *
     * @param uuid    the id of the user who is to be updated
     * @param request the new values of the user information
     * @return the updated user
     */
    @PatchMapping("/users/{uuid}")
    public UserDto updateUser(@PathVariable UUID uuid, @RequestBody UpdateUserRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setId(uuid);
        User newUser = userService.updateUser(user);
        return userAggregator.populate(newUser);
    }

    /**
     * getAvatarUploadUrl generate a URL for uploading the user avatar.
     *
     * @param uuid uuid of the user who want to upload his/her avatar
     * @return {@link com.joyjoin.userservice.controller.dto.GetAvatarUploadUrlResponse},
     * containing both the reference to this newly uploaded image and the URL to upload to.
     */
    @GetMapping("/users/{uuid}/upload_avatar")
    public GetAvatarUploadUrlResponse getAvatarUploadUrl(@PathVariable UUID uuid) {
        var expireTime = LocalDateTime.now().plusMinutes(30);
        return new GetAvatarUploadUrlResponse(userService.getAvatarUploadInformation(uuid, expireTime));
    }

//    /**
//     * getUserByUUID returns the information of user with specified uuid
//     *
//     * @param uuid uuid of the desired user
//     * @return user information
//     */
//    @GetMapping("/users/{uuid}")
//    public UserDto getUserByUUID(@PathVariable UUID uuid) {
//        return userAggregator.aggregate(uuid);
//    }

    /**
     * getUsers returns the information of a batch of users with specified uuids
     *
     * @param uuids a list of uuids of the desired users
     * @return a list of uuid to user information
     */
    @GetMapping("/users/{uuids}")
    public List<UserDto> getUsers(@PathVariable List<UUID> uuids) {
        return userAggregator.batchedAggregate(uuids);
    }

    /**
     * the request param is passed in the URL like this: http://localhost:8086/user/by-email?email=josip97@gmail.com
     *
     * @param email email of the desired user
     * @return UserDto
     */
    @GetMapping("/by-email")
    public UserDto getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return userAggregator.populate(user);
    }
}
