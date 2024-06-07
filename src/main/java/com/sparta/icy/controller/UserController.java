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
@RequestMapping("/users")
public class UserController {


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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(requestDto.getUsername());
        final String accessToken = jwtUtil.generateToken(userDetails.getUsername(), true);
        final String refreshTokenString = jwtUtil.generateToken(userDetails.getUsername(), false);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setUser((User) userDetails);
        refreshToken.setExpiryDate(LocalDateTime.now().plusWeeks(2));
        refreshTokenService.save(refreshToken);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshTokenString));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        return ResponseEntity.ok("Logged out successfully");
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
