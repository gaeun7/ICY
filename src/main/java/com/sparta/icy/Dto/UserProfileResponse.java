package com.sparta.icy.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {
    private String username;
    private String nickname;
    private String email;
    private String intro;

    public UserProfileResponse(String username, String nickname, String email, String intro) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.intro = intro;
    }
}
