package com.sparta.icy.controller;

import com.sparta.icy.dto.SignupRequestDto;
import com.sparta.icy.dto.UserProfileResponse;
import com.sparta.icy.dto.UserUpdateRequest;
import com.sparta.icy.entity.User;
import com.sparta.icy.security.UserDetailsImpl;
import com.sparta.icy.service.UserService;
import com.sparta.icy.error.AlreadySignedOutUserCannotBeSignoutAgainException;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
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
    @GetMapping("/login-success")
    public String mainPage() {
        return "index";
    }


    @PostMapping("/sign-up")
    public String signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "회원 가입 실패";
        }
        userService.signup(requestDto);

        return "회원가입 성공";
    }

    @PatchMapping("/sign-out")
    public String signout(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody String password) {
        User user=userDetails.getUser();
        boolean result= userService.signout(user.getUsername(), password);
        //탈퇴 실패
        if(!result){
            return "탈퇴 실패";
        }
        //탈퇴 성공
        return "탈퇴 성공";
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        userService.logout(response);
        return ResponseEntity.ok("User logged out successfully");
    }
}
