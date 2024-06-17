package com.sparta.icy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.icy.dto.NewsfeedDto;
import com.sparta.icy.dto.NewsfeedResponseDto;
import com.sparta.icy.service.NewsfeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsfeedController.class)
public class NewsfeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsfeedService newsfeedService;

    @Autowired
    private ObjectMapper objectMapper;

    private NewsfeedDto newsfeedDto;
    private NewsfeedResponseDto newsfeedResponseDto;

    @BeforeEach
    void setUp() {
        newsfeedDto = new NewsfeedDto();
        newsfeedDto.setTitle("테스트 뉴스피드 제목");
        newsfeedDto.setRecruitmentCount(5);
        newsfeedDto.setContent("테스트 뉴스피드 내용");

        newsfeedResponseDto = new NewsfeedResponseDto(
                1L,
                "테스트 뉴스피드 제목",
                5,
                "테스트 뉴스피드 내용",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void createNewsfeed() throws Exception {
        // given
        NewsfeedDto newsfeedDto = new NewsfeedDto("테스트 뉴스피드 제목", 5, "테스트 뉴스피드 내용");
        Mockito.doNothing().when(newsfeedService).createNewsfeed(any(NewsfeedDto.class));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsfeedDto))
                        .with(csrf()))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    void getNewsfeed() throws Exception {
        // given
        Mockito.when(newsfeedService.getNewsfeed(anyLong())).thenReturn(newsfeedResponseDto);

        // when
        mockMvc.perform(get("/boards/{id}", 1L))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("테스트 뉴스피드 제목"))
                .andExpect(jsonPath("$.recruitmentCount").value(5))
                .andExpect(jsonPath("$.content").value("테스트 뉴스피드 내용"))
                .andExpect(jsonPath("$.created_at").isNotEmpty())
                .andExpect(jsonPath("$.updated_at").isNotEmpty())
                .andExpect(jsonPath("$.user_id").value(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateNewsfeed() throws Exception {
        // given
        NewsfeedDto newsfeedDto = new NewsfeedDto("테스트 뉴스피드 제목", 5, "테스트 뉴스피드 내용");
        Mockito.doNothing().when(newsfeedService).updateNewsfeed(anyLong(), any(NewsfeedDto.class));

        // when
        mockMvc.perform(put("/boards/{feedId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsfeedDto))
                        .with(csrf()))
                // then
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    void deleteNewsfeed() throws Exception {
        // given
        Mockito.doNothing().when(newsfeedService).deleteNewsfeed(anyLong());

        // when
        mockMvc.perform(delete("/boards/{feedId}", 1L)
                        .with(csrf()))
                // then
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    void getAllNewsfeed() throws Exception {
        // given
        List<NewsfeedResponseDto> newsfeeds = Arrays.asList(newsfeedResponseDto);
        Mockito.when(newsfeedService.getAllNewsfeed()).thenReturn(newsfeeds);

        // when
        mockMvc.perform(get("/boards"))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("테스트 뉴스피드 제목"))
                .andExpect(jsonPath("$[0].recruitmentCount").value(5))
                .andExpect(jsonPath("$[0].content").value("테스트 뉴스피드 내용"));
    }

}