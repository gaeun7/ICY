package com.sparta.icy.controller;

import com.sparta.icy.dto.AuthResponse;
import com.sparta.icy.dto.LoginRequestDto;
import com.sparta.icy.dto.UserRequestDto;
import com.sparta.icy.entity.RefreshToken;
import com.sparta.icy.entity.User;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.service.AuthService;
import com.sparta.icy.service.CustomUserDetailsService;
import com.sparta.icy.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
   @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity<UserRequestDto> register(@RequestBody UserRequestDto requestDto){
        return null;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
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
    public String signout(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody SignoutRequestDto signoutRequestDto) {
        User user=userDetails.getUser();
        boolean result= userService.signout(user.getUsername(), signoutRequestDto);
        //탈퇴 실패
        if(!result){
            return "탈퇴 실패";
        }
        //탈퇴 성공
        return "탈퇴 성공";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        String token = jwtUtil.createToken(requestDto.getUsername(), null, true);
        jwtUtil.addJwtToCookie(token, response);

        // 로그 추가
        logService.addLoginLog(requestDto.getUsername());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserUpdateRequest req) {
        User updatedUser = userService.updateUser(id, req);
        return ResponseEntity.ok(updatedUser);
    }


}
