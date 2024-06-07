package com.sparta.icy.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class UserUpdateRequest {
    private String nickname;
    private String intro;
    private String currentPassword;
    private String newPassword;
}