package com.sparta.icy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserUpdateRequestTest {

    @Test
    public void testUserUpdateRequestCreation() {
        // Given
        String expectedNickname = "newNick";
        String expectedIntro = "newIntro";
        String expectedCurrentPassword = "currentPassword";
        String expectedNewPassword = "newPassword";

        // When
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setNickname(expectedNickname);
        userUpdateRequest.setIntro(expectedIntro);
        userUpdateRequest.setCurrentPassword(expectedCurrentPassword);
        userUpdateRequest.setNewPassword(expectedNewPassword);

        // Then
        assertEquals(expectedNickname, userUpdateRequest.getNickname());
        assertEquals(expectedIntro, userUpdateRequest.getIntro());
        assertEquals(expectedCurrentPassword, userUpdateRequest.getCurrentPassword());
        assertEquals(expectedNewPassword, userUpdateRequest.getNewPassword());
    }

    @Test
    public void testUserUpdateRequestNullValues() {
        // Given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUpdateRequest.setNickname(null);
        });
        assertEquals("닉네임을 작성해주세요", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            userUpdateRequest.setIntro(null);
        });
        assertEquals("한 줄 소개를 작성해주세요", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            userUpdateRequest.setCurrentPassword(null);
        });
        assertEquals("비밀번호를 입력해주세요", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            userUpdateRequest.setNewPassword(null);
        });
        assertEquals("새로운 비밀번호를 입력해주세요", exception.getMessage());
    }
}