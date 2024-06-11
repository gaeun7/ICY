package com.sparta.icy.controller;

import com.sparta.icy.dto.LoginRequestDto;
import com.sparta.icy.service.LogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;


    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        System.out.println("로그인 요청 수신: " + loginRequestDto.getUsername());

        logService.login(loginRequestDto, response);

        // 로그인 성공 시 로그 추가
        //logService.addLog(loginRequestDto.getUsername(), "로그인");


        return ResponseEntity.ok("로그인에 성공하였습니다.");
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        logService.logout(response);
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @PostMapping("/addLoginLog")
    public ResponseEntity<String> addLoginLog(@RequestBody String username) {
        logService.addLog(username, "로그인"); // 로그인 로그 추가
        return ResponseEntity.ok("로그 추가 완료");
    }
}