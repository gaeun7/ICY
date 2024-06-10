package com.sparta.icy.dto;

import com.sparta.icy.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String intro;
    private Status status;
}
