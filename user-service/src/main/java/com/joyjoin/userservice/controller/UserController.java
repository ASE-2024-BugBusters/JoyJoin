package com.joyjoin.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.userservice.dto.UpdateUserRequest;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.userservice.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user")
@Validated
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    /**
     * this can only be user for testing, in production needs to be deleted
     *
     * @return
     */
    @GetMapping()
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PatchMapping("/{uuid}")
    public UserDto updateUser(@PathVariable UUID uuid, @RequestBody UpdateUserRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setId(uuid);
        return userService.updateUser(user);
    }

    @GetMapping("/{uuid}")
    public UserDto getUserByUUID(@PathVariable UUID uuid) {
        return userService.getUser(uuid);
    }

    @GetMapping("/{uuid}/name")
    public UserDto getUserByName(@PathVariable UUID uuid) {
        return userService.getUser(uuid);
    }

    /**
     * the request param is passed in the URL like this: http://localhost:8085/user/by-email?email=josip97@gmail.com
     *
     * @param email
     * @return UserDto
     */
    @GetMapping("/by-email")
    public UserDto getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
