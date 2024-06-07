package com.sparta.icy.config;

import com.sparta.icy.filter.JwtRequestFilter;
import com.sparta.icy.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j(topic = "Spring Security 설정을 구성, 특정 엔드포인트에 대한 권한 설정, JWT 필터를 등록하여 각 요청에 대한 인증 및 권한 부여를 처리")
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return authentication -> {
            throw new IllegalStateException(("Bean 객체가 제대로 생성되지 않았습니다."));
        };
    }

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // csrf 설정
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 리소스 접근 허용 설정
//                                .requestMatchers("/users/register").permitAll() 회원가입 주소
//                                .requestMatchers("/users/login").permitAll()  로그인 주소
                                .requestMatchers(HttpMethod.GET).permitAll() // GET 접근 허용
                                .requestMatchers(HttpMethod.POST).permitAll()
                                .anyRequest().authenticated() // 모든 요청 인증 처리하기
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}