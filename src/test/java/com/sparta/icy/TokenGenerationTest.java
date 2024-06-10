//package com.sparta.icy;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.icy.dto.LoginRequestDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class TokenGenerationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testTokenGeneration() throws Exception {
//        String url = "/users/login";
//
//        // 로그인 요청에 사용될 사용자 정보 생성
//        LoginRequestDto requestDto = new LoginRequestDto();
//        requestDto.setUsername("username");
//        requestDto.setPassword("password");
//
//        // 로그인 요청 보내기
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestDto), headers);
//        ResponseEntity<AuthResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AuthResponse.class);
//
//        // 응답 확인
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            AuthResponse authResponse = responseEntity.getBody();
//            System.out.println("Access Token: " + authResponse.getAccessToken());
//            System.out.println("Refresh Token: " + authResponse.getRefreshToken());
//        } else {
//            System.out.println("Login failed. Status code: " + responseEntity.getStatusCodeValue());
//        }
//    }
//}
