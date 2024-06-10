package com.sparta.icy.service;

import com.sparta.icy.dto.LoginRequestDto;
import com.sparta.icy.entity.Log;
import com.sparta.icy.entity.User;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.repository.LogRepository;
import com.sparta.icy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public void addLog(String username, String action) {
        Log log = new Log(username, action);
        logRepository.save(log);
    }

    public String login(LoginRequestDto dto, HttpServletResponse res) {

        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(()->
                new EntityNotFoundException("없지롱"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new PasswordDoesNotMatchException("비밀번호 일치하지 않음");
        }

        // 토큰 생성
        String token = jwtUtil.createToken(dto.getUsername(), true);
        jwtUtil.addJwtToCookie(token, res);

//        String token = jwtUtil.createToken(user);
//        jwtUtil.addJwtToCookie(token, res);
        return token;
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void addLoginLog(String username) {
        addLog(username, "로그인");
    }

    public void addLogoutLog(String username) {
        addLog(username, "로그아웃");
    }


}
