package com.sparta.icy.controller;

import com.sparta.icy.dto.SignupRequestDto;
import com.sparta.icy.entity.User;
import com.sparta.icy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // private final JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/users/sign-up";
        }

        userService.signup(requestDto);

        return "redirect:/api/users/login";
    }

    //이 주석은 UserDetailsImpl과 UserDetailsImplService가 구현되면 해제해주세요
//    @PatchMapping("/sign-out")
//    public String signout(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody String password) {
//        User user=userDetails.getUser();
//       boolean result= userService.signout(user.getUsername(), password);
//       //탈퇴 실패
//      if(!result){
//          return "redirect:/api/users/sign-out";
//      }
//       //탈퇴 성공
//        return "redirect:/api/users/sign-in";
//    }

}
