package com.sparta.icy.controller;

import com.sparta.icy.dto.*;
import com.sparta.icy.entity.User;
import com.sparta.icy.service.AuthService;
//import com.sparta.icy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

//    @Autowired
//    private UserService userService;
    @Autowired
    private AuthService authService;

//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @PostMapping("/register")
    public ResponseEntity<UserRequestDto> register(@RequestBody UserRequestDto requestDto){
        return null; //회원가입
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequestDto requestDto) {
        return authService.authenticate(requestDto.getUsername(), requestDto.getPassword());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<UserProfileResponse> getUser(@PathVariable long id) {
//        return ResponseEntity.ok(userService.getUser(id)); //프로필 조회
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserUpdateRequest req) {
//        return ResponseEntity.ok(userService.updateUser(id, req)); //프로필 수정
//    }

}
