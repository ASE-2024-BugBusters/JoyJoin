package com.joyjoin.userservice.controller;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public UserDto createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{uuid}")
    public UserDto getUserByUUID(@PathVariable UUID uuid) {
        return userService.getUser(uuid);
    }

    /**
     * the request param is passed in the URL like this: http://localhost:8085/user/by-email?email=josip97@gmail.com
     * @param email
     * @return UserDto
     */
    @GetMapping("/by-email")
    public UserDto getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
