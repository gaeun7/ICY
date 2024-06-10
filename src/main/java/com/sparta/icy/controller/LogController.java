package com.sparta.icy.controller;

import com.sparta.icy.dto.LoginRequestDto;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.service.LogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final LogService logService;

    public LogController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, LogService logService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.logService = logService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        System.out.println("로그인 요청 수신: " + loginRequestDto.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 로그인 성공 시 로그 추가
        logService.addLog(loginRequestDto.getUsername(), "로그인");

        // 토큰 생성
        String token = jwtUtil.createToken(loginRequestDto.getUsername(), null, true);
        jwtUtil.addJwtToCookie(token, response);
        return ResponseEntity.ok("로그인에 성공하였습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
}