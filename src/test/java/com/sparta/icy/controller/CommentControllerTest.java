package com.sparta.icy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.icy.dto.CommentRequestDto;
import com.sparta.icy.dto.CommentResponseDto;
import com.sparta.icy.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentRequestDto commentRequestDto;
    private CommentResponseDto commentResponseDto;

    @BeforeEach
    void setUp() {
        commentRequestDto = new CommentRequestDto("테스트 댓글");

        commentResponseDto = new CommentResponseDto(
                1L,
                "테스트 댓글 응답",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void writeComment() throws Exception {
        // given
        Mockito.when(commentService.writeComment(anyLong(), any(CommentRequestDto.class)))
                .thenReturn(commentResponseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{feedId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("테스트 댓글 응답"));
    }

    @Test
    void getComments() throws Exception {
        // given
        List<CommentResponseDto> comments = Arrays.asList(commentResponseDto);
        Mockito.when(commentService.getComments(anyLong())).thenReturn(comments);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{feedId}", 1L))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("테스트 댓글 응답"));
    }

    @Test
    void updateComment() throws Exception {
        // given
        Mockito.when(commentService.updateComment(anyLong(), any(CommentRequestDto.class)))
                .thenReturn(commentResponseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRequestDto("업데이트된 댓글"))))

                // then (
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("테스트 댓글 응답"));
    }

    @Test
    void deleteComment() throws Exception {
        // given
        Mockito.doNothing().when(commentService).deleteComment(anyLong());

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{commentId}", 1L))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void writeCommentThrowsException() throws Exception {
        // given
        Mockito.when(commentService.writeComment(anyLong(), any(CommentRequestDto.class)))
                .thenThrow(new IllegalArgumentException("유효하지 않은 데이터"));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{feedId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRequestDto("테스트 댓글"))))

                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}