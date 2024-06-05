package com.sparta.icy.controller;

import com.sparta.icy.dto.UserProfileResponse;
import com.sparta.icy.dto.UserUpdateRequest;
import com.sparta.icy.entity.User;
import com.sparta.icy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserUpdateRequest req) {
        return ResponseEntity.ok(userService.updateUser(id, req));
    }

}
