package com.sparta.icy.service;

import com.sparta.icy.dto.AuthResponse;
import com.sparta.icy.dto.UserRequestDto;
import com.sparta.icy.entity.User;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "사용자의 로그인 요청을 처리, 인증이 성공하면 JWT를 생성하여 클라이언트에게 반환. 또한 Refresh Token을 생성하고 검증하여 새로운 Access Token을 발급하는 역할도 수행")
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UserRepository userRepository;

    public AuthResponse authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        final String accessToken = jwtUtil.generateToken(username, true);
        final String refreshToken = jwtUtil.generateToken(username, false);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh Token is expired");
        }

        final String username = jwtUtil.extractUsername(refreshToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        final String newAccessToken = jwtUtil.generateToken(username, true);
        final String newRefreshToken = jwtUtil.generateToken(username, false);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public User registerUser(UserRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
//        user.setPassword(passwordEncoder.encode(requestDto.getPassword())); 비번 암호화 해야함.
        user.setEmail(requestDto.getEmail());
        return userRepository.save(user);
    }
}
