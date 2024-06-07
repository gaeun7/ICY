package com.sparta.icy.Controller;

import com.sparta.icy.Dto.LoginRequestDto;
import com.sparta.icy.Dto.SignupRequestDto;
import com.sparta.icy.Dto.UserProfileResponse;
import com.sparta.icy.Dto.UserUpdateRequest;
import com.sparta.icy.Entity.User;
import com.sparta.icy.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
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

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login-success")
    public String mainPage() {
        return "index";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute SignupRequestDto requestDto) {
        try {
            userService.signup(requestDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }
}