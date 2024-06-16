package com.sparta.icy.entity;

import com.sparta.icy.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        // Given
        User user = new User("testUser", "testNick", "password", "test@example.com", "intro", UserStatus.IN_ACTION);

        // When

        // Then
        assertEquals("testUser", user.getUsername());
        assertEquals("testNick", user.getNickname());
        assertEquals("password", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("intro", user.getIntro());
        assertEquals(UserStatus.IN_ACTION.getStatus(), user.getStatus());
    }

    @Test
    public void testUserUpdate() {
        // Given
        User user = new User("testUser", "testNick", "password", "test@example.com", "intro", UserStatus.IN_ACTION);
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setNickname("newNick");
        updateRequest.setIntro("newIntro");
        updateRequest.setNewPassword("newPassword");

        // When
        user.update(updateRequest);

        // Then
        assertEquals("newNick", user.getNickname());
        assertEquals("newIntro", user.getIntro());
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    public void testUserUpdateWithNullRequest() {
        // Given
        User user = new User("testUser", "testNick", "password", "test@example.com", "intro", UserStatus.IN_ACTION);
        UserUpdateRequest updateRequest = null;

        // When
        Exception exception = assertThrows(NullPointerException.class, () -> {
            user.update(updateRequest);
        });

        // Then
        assertNotNull(exception);
    }

    @Test
    public void testUserStatusEnums() {
        assertEquals("정상", UserStatus.IN_ACTION.getStatus());
        assertEquals("탈퇴", UserStatus.SECESSION.getStatus());
    }
}