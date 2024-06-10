package com.sparta.icy.config;

import com.sparta.icy.controller.LogController;
import com.sparta.icy.jwt.JwtAuthenticationFilter;
import com.sparta.icy.jwt.JwtAuthorizationFilter;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.security.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

@Service
@ComponentScan(basePackages = ("com.sparta"))
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(LogController logController) throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, logController); // LogController 인스턴스를 생성자에 추가
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식 대신 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 리소스에 대한 접근 허용
                        .requestMatchers("/user/**", "/log/**").permitAll() //
                        .requestMatchers("/newsfeed").permitAll() // 모든 사용자에게 뉴스피드 조회 허용
                        .requestMatchers("/newsfeed/*").permitAll() // 모든 사용자에게 특정 게시물 조회 허용
                        .requestMatchers("/newsfeed/create").authenticated() // 게시물 작성, 수정, 삭제는 인증 필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
        );

        // 필터 추가
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);


        return http.build();
    }
}