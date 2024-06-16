package com.sparta.icy.entity;

import com.sparta.icy.dto.CommentRequestDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        // Given
        User user = new User("testUser", "testNick", "password", "test@example.com", "intro", UserStatus.IN_ACTION);
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setUser(user);
        CommentRequestDto commentRequestDto = new CommentRequestDto("Test Comment");

        // When
        Comment comment = new Comment(commentRequestDto);
        comment.setUser(user);
        comment.setNewsfeed(newsfeed);

        // Then
        assertEquals("Test Comment", comment.getContent());
        assertEquals(user, comment.getUser());
        assertEquals(newsfeed, comment.getNewsfeed());
        assertNotNull(comment.getCreated_at());
        assertNotNull(comment.getUpdated_at());
    }

    @Test
    public void testCommentUpdate() {
        // Given
        User user = new User("testUser", "testNick", "password", "test@example.com", "intro", UserStatus.IN_ACTION);
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setUser(user);
        CommentRequestDto commentRequestDto = new CommentRequestDto("Test Comment");
        Comment comment = new Comment(commentRequestDto);
        comment.setUser(user);
        comment.setNewsfeed(newsfeed);

        // When
        comment.setContent("Updated Comment");

        // Then
        assertEquals("Updated Comment", comment.getContent());
    }
}