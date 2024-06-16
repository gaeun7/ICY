package com.sparta.icy.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class NewsfeedTest {

    @Test
    public void testNewsfeedCreation() {
        // Given
        User user = new User("testUser",
                "testNick",
                "password",
                "test@example.com",
                "intro",
                UserStatus.IN_ACTION); // Using IN_ACTION instead of ACTIVE
        LocalDateTime now = LocalDateTime.now();
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setUser(user);
        newsfeed.setTitle("Test Title");
        newsfeed.setRecruitmentCount(5);
        newsfeed.setContent("Test Content");
        newsfeed.setCreated_at(now);
        newsfeed.setUpdated_at(now);
        newsfeed.setComments(Collections.emptyList());

        // When

        // Then
        assertEquals("Test Title", newsfeed.getTitle());
        assertEquals(5, newsfeed.getRecruitmentCount());
        assertEquals("Test Content", newsfeed.getContent());
        assertEquals(user, newsfeed.getUser());
        assertEquals(now, newsfeed.getCreated_at());
        assertEquals(now, newsfeed.getUpdated_at());
        assertTrue(newsfeed.getComments().isEmpty());
    }

    @Test
    public void testNewsfeedUpdate() {
        // Given
        User user = new User("testUser",
                "testNick",
                "password",
                "test@example.com",
                "intro",
                UserStatus.IN_ACTION); // Using IN_ACTION instead of ACTIVE
        LocalDateTime now = LocalDateTime.now();
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setUser(user);
        newsfeed.setTitle("Test Title");
        newsfeed.setRecruitmentCount(5);
        newsfeed.setContent("Test Content");
        newsfeed.setCreated_at(now);
        newsfeed.setUpdated_at(now);
        newsfeed.setComments(Collections.emptyList());

        // When
        newsfeed.setContent("Updated Content");

        // Then
        assertEquals("Updated Content", newsfeed.getContent());
    }
}