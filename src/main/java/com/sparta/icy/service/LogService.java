package com.sparta.icy.service;

import com.sparta.icy.dto.LoginRequestDto;
import com.sparta.icy.entity.Log;
import com.sparta.icy.entity.RefreshToken;
import com.sparta.icy.entity.User;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.repository.LogRepository;
import com.sparta.icy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.CollationKey;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

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

        // 리프레시 토큰 확인 및 생성과 갱신
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByUser(user);
        RefreshToken refreshToken;
        if (optionalRefreshToken.isPresent()) {
            refreshToken = optionalRefreshToken.get();
            // 리프레시 토큰 갱신 (필요 시 만료 시간 등을 업데이트)
            refreshToken.setExpiryDate(LocalDateTime.now().plusWeeks(2));
        } else {
            refreshToken = refreshTokenService.createRefreshToken(user);
        }
        refreshTokenService.save(refreshToken);

        jwtUtil.addRefreshTokenToCookie(refreshToken.getToken(), res);

        jwtUtil.addJwtToCookie(token, res);

        return token;
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        Cookie refreshCookie = new Cookie(JwtUtil.REFRESH_TOKEN_HEADER, null);

        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.addCookie(refreshCookie);
    }

    public void addLoginLog(String username) {
        addLog(username, "로그인");
    }

    public void addLogoutLog(String username) {
        addLog(username, "로그아웃");
    }


}
