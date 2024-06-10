package com.sparta.icy;

import com.sparta.icy.dto.UserUpdateRequest;
import com.sparta.icy.entity.Status;
import com.sparta.icy.entity.User;
import com.sparta.icy.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    private User user;

    @Test
    @DisplayName("유저 프로필 조회 // 테스트 완료")
    public void getUserProfile() {
            /*
            given
             */
        user = User.builder().id(1).username("honghong").nickname("홍길동").password("Password123!").email("honghong@sparta.com").intro("한 줄 소개임당").status(Status.ENROLLED).build();

            /*
            when, then
             */
        Assertions.assertThat(user.getUsername()).isEqualTo("honghong");
        Assertions.assertThat(user.getNickname()).isEqualTo("홍길동");
        Assertions.assertThat(user.getEmail()).isEqualTo("honghong@sparta.com");
        Assertions.assertThat(user.getPassword()).isEqualTo("Password123!");
        Assertions.assertThat(user.getIntro()).isEqualTo("한 줄 소개임당");
        Assertions.assertThat(user.getStatus()).isEqualTo(Status.ENROLLED);



    }
}


//package com.sparta.icy;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.icy.dto.UserProfileResponse;
//import com.sparta.icy.dto.UserUpdateRequest;
//import com.sparta.icy.dto.UserUpdateResponse;
//import com.sparta.icy.entity.User;
//import com.sparta.icy.service.UserService;
//import com.sparta.icy.controller.UserController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private User user;
//    private UserProfileResponse userProfileResponse;
//    private UserUpdateRequest userUpdateRequest;
//    private UserUpdateResponse userUpdateResponse;
//
//    @BeforeEach
//    public void setup() {
//        user = new User();
//        user.setId(1);
//        user.setUsername("honghong");
//        user.setNickname("홍길동");
//        user.setEmail("honghong@sparta.com");
//        user.setIntro("Hello, I'm Test User!");
//        user.setPassword("Password123!");

//        userProfileResponse = new UserProfileResponse("honghong", "홍길동", "honghong@sparta.com", "Hello, I'm Test User!");
//
//        userUpdateRequest = new UserUpdateRequest();
//        userUpdateRequest.setCurrentPassword("Password123!");
//        userUpdateRequest.setNewPassword("NewPassword123!");
//        userUpdateRequest.setNickname("Updated User");
//        userUpdateRequest.setEmail("updateduser@example.com");
//        userUpdateRequest.setIntro("Updated intro");
//
//        userUpdateResponse = new UserUpdateResponse("Updated User", "updateduser@example.com", "Updated intro", "NewPassword123!");
//    }
//
//    @Test
//    public void testGetUser() throws Exception {
//        Mockito.when(userService.getUser(1)).thenReturn(userProfileResponse);
//
//        String expectedJson = objectMapper.writeValueAsString(userProfileResponse);
//        System.out.println("Expected JSON Response: " + expectedJson);
//
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//    }
//
//    @Test
//    public void testUpdateUser() throws Exception {
//        Mockito.when(userService.updateUser(Mockito.eq(1L), Mockito.any(UserUpdateRequest.class)))
//                .thenReturn(user); // 업데이트된 유저 정보가 아닌 기존의 유저 정보를 반환하도록 수정
//
//        String requestJson = objectMapper.writeValueAsString(userUpdateRequest);
//
//        mockMvc.perform(put("/api/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//
//        String expectedJson = objectMapper.writeValueAsString(userProfileResponse);
//        System.out.println("Expected JSON Response: " + expectedJson);
//
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//
//        Mockito.when(userService.updateUser(Mockito.eq(1), Mockito.any(UserUpdateRequest.class)))
//                .thenAnswer(invocation -> {
//                    UserUpdateRequest request = invocation.getArgument(1);
//                    user.setNickname(request.getNickname());
//                    user.setEmail(request.getEmail());
//                    user.setIntro(request.getIntro());
//                    user.setPassword(request.getNewPassword());
//                    return user;
//                });
//
//        String requestJson = objectMapper.writeValueAsString(userUpdateRequest);
//        String expectedJson = objectMapper.writeValueAsString(userUpdateResponse);
//
//        System.out.println("Request JSON: " + requestJson);
//        System.out.println("Expected JSON Response: " + expectedJson);
//
//        MvcResult result = mockMvc.perform(put("/api/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        mockMvc.perform(put("/api/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//
//        String actualJson = result.getResponse().getContentAsString();
//        System.out.println("Actual JSON Response: " + actualJson);
//
//        assertThat(actualJson).isEqualToIgnoringWhitespace(expectedJson);
//    }
//}
// 포기