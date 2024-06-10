package com.sparta.icy.controller;

import com.sparta.icy.dto.LoginRequestDto;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

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

        // 클라이언트에게 토큰 전달
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 로그아웃 시 현재 사용자 정보를 가져와 로그 추가
        String username = jwtUtil.getUsernameFromRequest(request);
        logService.addLog(username, "로그아웃");

        // 클라이언트에게 성공 메시지 전달
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/add-login-log")
    public ResponseEntity<String> addLoginLog(@RequestBody String username) {
        logService.addLog(username, "로그인"); // 로그인 로그 추가
        return ResponseEntity.ok("로그 추가 완료");
    }
}
