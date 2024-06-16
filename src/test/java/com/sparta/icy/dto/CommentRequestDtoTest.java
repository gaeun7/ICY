package com.sparta.icy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentRequestDtoTest {

    @Test
    public void testCommentRequestDtoCreation() {
        // Given
        String expectedContent = "Test Comment";
        CommentRequestDto commentRequestDto = new CommentRequestDto(expectedContent);

        // When
        String actualContent = commentRequestDto.getContent();

        // Then
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testCommentRequestDtoNullContent() {
        // Given
        String content = null;

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CommentRequestDto(content);
        });

        // Then
        assertNotNull(exception);
        assertEquals("내용을 입력해주세요.", exception.getMessage());
    }
}