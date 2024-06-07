package com.sparta.icy.Dto;

import com.sparta.icy.Entity.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private String intro;
    private String nickname;
}
